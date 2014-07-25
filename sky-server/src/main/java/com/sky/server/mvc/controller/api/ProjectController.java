package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jcooky on 2014. 7. 25..
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {
  private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

  @RequestMapping(method = RequestMethod.POST)
  public Project create(@RequestBody Project project) {
    logger.debug("jarFile");
    return project;
  }

  @RequestMapping(method = RequestMethod.PUT)
  public void put(@RequestBody Project project) {
    logger.debug("project: {}", project.getName());
    throw new UnsupportedOperationException("test");
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleException(UnsupportedOperationException e) {
    return e.getMessage();
  }
}
