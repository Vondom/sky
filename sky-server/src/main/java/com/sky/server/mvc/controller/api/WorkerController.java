package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jcooky on 2014. 8. 6..
 */
@RestController
@RequestMapping(value = "/api/worker")
public class WorkerController {

  private final static Logger logger = LoggerFactory.getLogger(WorkerController.class);

  @Autowired
  private WorkerService workerService;

  @RequestMapping(method = RequestMethod.GET)
  public List<?> list() {
    return workerService.list();
  }
}
