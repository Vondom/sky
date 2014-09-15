package com.sky.server.mvc.service.thrift;

import com.sky.commons.WorkerControlService;
import com.sky.server.config.annotation.TService;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.service.WorkerService;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;

/**
 * Created by jcooky on 2014. 8. 3..
 */
@TService(name = "worker-control", thrift = WorkerControlService.class)
public class WorkerControlServiceImpl implements WorkerControlService.Iface {
  @Autowired
  private ServletRequest request;

  @Autowired
  private WorkerService workerService;

  @Autowired
  private WorkRepository workRepository;

  @Override
  @Transactional
  public long add(String address, int port) throws TException {
    if ("0.0.0.0".equals(address)) {
      address = request.getRemoteAddr();
    }

    Worker worker = workerService.get(address, port);
    if (worker == null) {
      worker = new Worker();
      worker.setAddress(address);
      worker.setPort(port);
    }
    worker.setState(Worker.State.IDLE);
    return workerService.save(worker).getId();
  }

  @Override
  @Transactional(readOnly = true)
  public void done(long id) throws TException {

    Work work = workRepository.findReadyWork();
    if (work != null) {
      Worker worker = workerService.get(id);

      workerService.doWork(worker, work);
    }
  }

  @Override
  @Transactional
  public void remove(long id) throws TException {
    Worker worker = workerService.get(id);

    worker.setState(Worker.State.QUIT);
    workerService.save(worker);
  }
}
