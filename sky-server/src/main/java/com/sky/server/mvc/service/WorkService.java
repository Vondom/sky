package com.sky.server.mvc.service;

import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.repository.ExecutionUnitRepository;
import com.sky.server.mvc.repository.WorkRepository;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

/**
 * Created by jcooky on 2014. 8. 7..
 */
@Service
public class WorkService {
  private static final Logger logger = LoggerFactory.getLogger(WorkService.class);

  @Autowired
  private WorkRepository workRepository;

  @Autowired
  private WorkerService workerService;

  @Autowired
  private ExecutionUnitRepository executionUnitRepository;

  @Transactional
  public Work create(Work work) throws TException, ExecutionException, InterruptedException {
    logger.trace(".create(work={})", work);
    if (work.getExecutionUnit() != null)
      work.setExecutionUnit(executionUnitRepository.findOne(work.getExecutionUnit().getId()));
    work.setOrdering(workRepository.count());
    work = workRepository.save(work);

    workerService.doWork(work);

    return work;
  }

  public Work get(long id) {
    return workRepository.findOne(id);
  }

  public Work save(Work work) {
    return workRepository.save(work);
  }
}
