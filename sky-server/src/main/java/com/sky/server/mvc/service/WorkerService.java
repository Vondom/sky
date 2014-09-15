package com.sky.server.mvc.service;

import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.WorkerRepository;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by jcooky on 2014. 9. 12..
 */
@Service
public class WorkerService {
  @Autowired
  private WorkService workService;

  @Autowired
  private WorkerRepository workerRepository;

  @Async
  @Transactional(readOnly = true)
  public Future<Boolean> doWork(Work work) throws TException {
    List<Worker> workers = workerRepository.findByState(Worker.State.IDLE);

    if (workers.isEmpty())
      return new AsyncResult<Boolean>(false);

    doWork(workers.get(0), work);

    return new AsyncResult<Boolean>(true);
  }

  @Transactional(readOnly = true)
  public void doWork(Worker worker, final Work work) throws TException {

    connect(worker, new WorkerClientTemplateHandler() {
      @Override
      public void handle(com.sky.commons.Worker.Client worker) throws TException {
        worker.doWork(workService.toWork(work));
      }
    });
  }

  @Transactional
  private void connect(Worker worker, WorkerClientTemplateHandler handler) throws TException {
    if (worker.getState() == Worker.State.IDLE) {
      worker.setState(Worker.State.WORKING);
      workerRepository.save(worker);
    }
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

  private static interface WorkerClientTemplateHandler {
    void handle(com.sky.commons.Worker.Client worker) throws TException;
  }

}
