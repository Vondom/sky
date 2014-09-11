package com.sky.server.mvc.service.thrift;

import com.sky.commons.WorkerControlService;
import com.sky.server.config.annotation.TService;
import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.repository.WorkerRepository;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;

/**
 * Created by jcooky on 2014. 8. 3..
 */
@TService(name = "work-manager", thrift = WorkerControlService.class)
public class WorkManagerService implements WorkerControlService.Iface {
  @Autowired
  private ServletRequest request;

  @Autowired
  private WorkerRepository workerRepository;
  @Autowired
  private WorkRepository workRepository;

  @Override
  public long add(String address, int port) throws TException {
    Worker worker = workerRepository.findByAddressAndPort(address, port);
    if (worker == null) {
      worker = new Worker();
      if (!"0.0.0.0".equals(address)) {
        worker.setAddress(address);
      } else {
        worker.setAddress(request.getRemoteAddr());
      }
      worker.setPort(port);
      worker = save(worker);
    }
    return worker.getId();
  }

  @Override
  @Transactional
  public void done(long id) throws TException {
    Worker worker = workerRepository.findOne(id);

    workerRepository.delete(worker);
  }

  @Transactional
  private Worker save(Worker worker) {
    return workerRepository.save(worker);
  }
}
