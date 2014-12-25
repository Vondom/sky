package com.sky.server.service.thrift;

import com.sky.commons.Jar;
import com.sky.commons.Status;
import com.sky.commons.WorkerControlService;
import com.sky.server.config.annotation.TService;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.ExecutionUnitRepository;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.repository.WorkerRepository;
import com.sky.server.service.WorkerService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by jcooky on 2014. 8. 3..
 */
@TService(name = "worker-control", thrift = WorkerControlService.class)
public class WorkerControlServiceImpl implements WorkerControlService.Iface, WorkerService {
  @Autowired
  private ServletRequest request;

  @Autowired
  private WorkerRepository workerRepository;

  @Autowired
  private WorkRepository workRepository;

  @Autowired
  private ExecutionUnitRepository executionUnitRepository;

  @Override
  @Transactional
  public long add(String address, int port) throws TException {
    if ("0.0.0.0".equals(address)) {
      address = request.getRemoteAddr();
    }

    Worker worker = workerRepository.findByAddressAndPort(address, port);
    if (worker == null) {
      worker = new Worker();
      worker.setAddress(address);
      worker.setPort(port);
    }
    worker.setState(Worker.State.IDLE);
    return workerRepository.save(worker).getId();
  }

  @Override
  @Transactional
  public void done(long workerId, long workId) throws TException {
    Work work = workRepository.findOne(workId);
    work.setFinished(true);
    work.setAverageTime(work.getAverageTime()/(double)(work.getMethodLogs().size()));
    workRepository.save(work);

    work = workRepository.findReadyWork();
    if (work != null) {
      Worker worker = workerRepository.findOne(workerId);

      this.doWork(worker, work);
    } else {
      Worker worker = workerRepository.findOne(workerId);
      worker.setState(Worker.State.IDLE);
    }
  }

  @Override
  @Transactional
  public void remove(long id) throws TException {
    Worker worker = workerRepository.findOne(id);

    worker.setState(Worker.State.QUIT);
    workerRepository.save(worker);
  }

  private static final Logger logger = LoggerFactory.getLogger(WorkerService.class);


  @Scheduled(fixedRate = 10 * 60 * 1000)
  @Transactional
  public void checkWorkers() throws TException {
    for (Worker worker : workerRepository.findAll()) {
      final ThreadLocal<Worker.State> state = new ThreadLocal<Worker.State>();
      state.set(worker.getState());

      if (!Worker.State.QUIT.equals(state.get())) {
        TSocket socket = null;
        try {
          socket = new TSocket(worker.getAddress(), worker.getPort());
          socket.open();
          com.sky.commons.Worker.Client worker1 = new com.sky.commons.Worker.Client(new TCompactProtocol(new TFramedTransport(socket)));
          Status status = worker1.status();
          switch (status.getState()) {
            case IDLE:
              state.set(Worker.State.IDLE);
              break;
            case WORKING:
              state.set(Worker.State.WORKING);
              break;
          }
        } catch (TException e) {
          state.set(Worker.State.QUIT);
        } finally {
          if (socket != null && socket.isOpen())
            socket.close();
        }

        if (!state.get().equals(worker.getState())) {
          worker.setState(state.get());
          workerRepository.save(worker);
        }
      }
    }
  }

  @Async
  @Transactional
  public Future<Boolean> doWork(Work work) throws TException {
    logger.trace(".doWork(work={})", work);
    List<Worker> workers = workerRepository.findByState(Worker.State.IDLE);

    if (workers.isEmpty())
      return new AsyncResult<Boolean>(false);

    try {
      Worker worker = workers.get(0);
      this.doWork(worker, work);
    } catch (TException e) {
      logger.error(e.getMessage(), e);
      throw e;
    }

    return new AsyncResult<Boolean>(true);
  }

  @Transactional
  private void doWork(Worker worker, Work work) throws TException {

    TSocket socket = null;
    try {
      socket = new TSocket(worker.getAddress(), worker.getPort());
      socket.open();
      com.sky.commons.Worker.Client workerClient = new com.sky.commons.Worker.Client(new TCompactProtocol(new TFramedTransport(socket)));

      if (Worker.State.IDLE.equals(worker.getState())) {
        worker.setState(Worker.State.WORKING);
        worker = workerRepository.save(worker);

        work.setWorker(worker);
        work = workRepository.save(work);
        executionUnitRepository.save(work.getExecutionUnit());

        logger.trace("worker0={}", worker);
      }

      logger.trace(".handle(worker={}) - START", worker);
      workerClient.doWork(new com.sky.commons.Work()
          .setId(work.getId())
          .setJar(new Jar()
              .setFile(work.getExecutionUnit().getJarFile())
              .setName(work.getExecutionUnit().getJarFileName()))
          .setArguments(work.getExecutionUnit().getArguments()));
      logger.trace("END");
    } finally {
      if (socket != null && socket.isOpen())
        socket.close();
    }
  }


  private static interface WorkerClientTemplateHandler {
    void handle(com.sky.commons.Worker.Client worker) throws TException;
  }
}
