package com.sky.server.service.thrift;

import com.sky.commons.Jar;
import com.sky.commons.Status;
import com.sky.commons.WorkerControlService;
import com.sky.server.config.annotation.TService;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.model.Worker;
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
        try {
          connect(worker, new WorkerClientTemplateHandler() {
            @Override
            public void handle(com.sky.commons.Worker.Client worker) throws TException {

              Status status = worker.status();
              switch (status.getState()) {
                case IDLE:
                  state.set(Worker.State.IDLE);
                  break;
                case WORKING:
                  state.set(Worker.State.WORKING);
                  break;
              }
            }
          });
        } catch (TException e) {
          state.set(Worker.State.QUIT);
        }

        if (!state.get().equals(worker.getState())) {
          worker.setState(state.get());
          workerRepository.save(worker);
        }
      }
    }
  }

  @Async
  @Transactional(readOnly = true)
  public Future<Boolean> doWork(Work work) throws TException {
    logger.trace(".doWork(work={})", work);
    List<Worker> workers = workerRepository.findByState(Worker.State.IDLE);

    if (workers.isEmpty())
      return new AsyncResult<Boolean>(false);

    try {
      doWork(workers.get(0), work);
    } catch (TException e) {
      logger.error(e.getMessage(), e);
      throw e;
    }

    return new AsyncResult<Boolean>(true);
  }

  @Transactional(readOnly = true)
  private void doWork(final Worker worker_, final Work work) throws TException {

    connect(worker_, new WorkerClientTemplateHandler() {
      private Worker worker = worker_;

      @Override
      public void handle(com.sky.commons.Worker.Client workerClient) throws TException {

        if (Worker.State.IDLE.equals(worker.getState())) {
          worker.setState(Worker.State.WORKING);
          worker.getWorks().add(work);

          worker = workerRepository.save(worker);

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
      }
    });
  }

  @Transactional
  private void connect(Worker worker, WorkerClientTemplateHandler handler) throws TException {
    TSocket socket = null;
    try {
      socket = new TSocket(worker.getAddress(), worker.getPort());
      socket.open();
      handler.handle(new com.sky.commons.Worker.Client(new TCompactProtocol(new TFramedTransport(socket))));
    } finally {
      if (socket != null && socket.isOpen())
        socket.close();
    }
  }

  private static interface WorkerClientTemplateHandler {
    void handle(com.sky.commons.Worker.Client worker) throws TException;
  }
}
