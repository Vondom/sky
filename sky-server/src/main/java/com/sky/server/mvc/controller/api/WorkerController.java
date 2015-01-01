package com.sky.server.mvc.controller.api;

import com.sky.commons.model.Worker;
import com.sky.server.mvc.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jcooky on 2014. 8. 6..
 */
@RestController
@RequestMapping(value = "/api/worker")
public class WorkerController {

  private final static Logger logger = LoggerFactory.getLogger(WorkerController.class);

  @Autowired
  private WorkerRepository workerRepository;

  @RequestMapping(method = RequestMethod.POST)
  public Worker save(@RequestBody Worker worker) {
    return workerRepository.save(worker);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<?> list() {
    return workerRepository.findAll();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Worker get(@PathVariable long id) {
    return workerRepository.findOne(id);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public boolean delete(@PathVariable long id) {
    workerRepository.delete(id);
    return true;
  }
}
