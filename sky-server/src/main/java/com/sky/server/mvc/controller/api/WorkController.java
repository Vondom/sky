package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.service.WorkService;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jcooky on 2014. 8. 6..
 */
@RestController
@RequestMapping("/api/work")
public class WorkController {

  private final static Logger logger = LoggerFactory.getLogger(WorkController.class);

  @Autowired
  private WorkService workService;

  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  public Work create(@RequestBody Work work) throws TException {
    return workService.create(work);
  }

  @ExceptionHandler({TException.class})
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public void handleTException(TException e) {
    logger.error(e.getMessage(), e);
  }
}
