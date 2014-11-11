package com.sky.server.mvc.service;

import com.sky.commons.Jar;
import com.sky.commons.Status;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.repository.WorkerRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by jcooky on 2014. 9. 12..
 */
@Service
public class WorkerService {
  private static final Logger logger = LoggerFactory.getLogger(WorkerService.class);

  @Autowired
  private WorkerRepository workerRepository;
  @Autowired
  private WorkRepository workRepository;

  @Scheduled(fixedRate = 10 * 60 * 1000)
  @Transactional(readOnly = true)
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
          this.save(worker);
        }
      }
    }
  }

  public com.sky.commons.Work toWork(Work work) {
    com.sky.commons.Work work1 = new com.sky.commons.Work()
        .setId(work.getId())
        .setJar(new Jar()
            .setFile(work.getExecutionUnit().getJarFile())
            .setName(work.getExecutionUnit().getJarFileName()))
        .setArguments(work.getExecutionUnit().getArguments());

    logger.debug("Return Work: {}", work1);

    return work1;
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
  public void doWork(final Worker worker, final Work work) throws TException {

    connect(worker, new WorkerClientTemplateHandler() {
      @Override
      public void handle(com.sky.commons.Worker.Client workerClient) throws TException {

        if (Worker.State.IDLE.equals(worker.getState())) {
          worker.setState(Worker.State.WORKING);
          worker.getWorks().add(work);
          Worker worker0 = workerRepository.save(worker);
          logger.trace("worker0={}", worker0);
        }

        logger.trace(".handle(worker={}) - START", worker);
        workerClient.doWork(toWork(work));
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

  @Transactional
  public Worker save(Worker worker) {
    return workerRepository.save(worker);
  }

  @Transactional
  public void remove(Worker worker) {
    workerRepository.delete(worker);
  }

  @Transactional(readOnly = true)
  public Worker get(long id) {
    return workerRepository.findOne(id);
  }

  @Transactional(readOnly = true)
  public Worker get(String address, int port) {
    return workerRepository.findByAddressAndPort(address, port);
  }

  public List<Worker> list() {
    return workerRepository.findAll();
  }

  private static interface WorkerClientTemplateHandler {
    void handle(com.sky.commons.Worker.Client worker) throws TException;
  }

}
