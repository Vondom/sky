package com.sky.server.web.rest;

import com.sky.commons.domain.ExecutionUnit;
import com.sky.commons.domain.MethodLog;
import com.sky.commons.domain.Work;
import com.sky.server.domain.WorkRepository;
import com.sky.server.service.TaskDistributedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by jcooky on 2014. 8. 6..
 */
@RestController
@RequestMapping(value = "/api/work")
public class WorkController {

  private final static Logger logger = LoggerFactory.getLogger(WorkController.class);

  @Autowired
  private Environment env;

  @Autowired
  private WorkRepository workRepository;

  @Autowired
  private TaskDistributedQueue taskQueue;

  @RequestMapping(method = RequestMethod.POST)
  @Transactional
  public Work create(@RequestBody Work work) throws Exception {

//    if (ArrayUtils.contains(env.getActiveProfiles(), "dev"))
//      workerService.checkWorkers();

    logger.trace(".create(work={})", work);
    work = workRepository.save(work);

//    workerService.doWork(work);

    taskQueue.put(work.getId());

    return work;
  }

  @RequestMapping(value = "/{id}/method-logs", method = RequestMethod.GET)
  @Transactional(readOnly = true)
  public Collection<MethodLog> getMethodLogs(@PathVariable long id) {
    Collection<MethodLog> methodLogs = get(id).getMethodLogs();
    logger.trace("methodLogs={}", methodLogs);

    return methodLogs;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Work get(@PathVariable long id) {
    return workRepository.findOne(id);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public boolean delete(@PathVariable long id) {
    workRepository.delete(id);
    return true;
  }

  @RequestMapping(value = "/{id}/execution-unit", method = RequestMethod.GET)
  public ExecutionUnit getExecutionUnit(@PathVariable long id) {
    return workRepository.findOne(id).getExecutionUnit();
  }
}
