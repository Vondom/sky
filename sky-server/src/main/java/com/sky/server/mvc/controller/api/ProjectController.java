package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.Project;
import com.sky.server.mvc.service.ProjectService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by jcooky on 2014. 7. 25..
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {
  private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

  @Autowired
  private ProjectService projectService;

  @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"})
  public Project create(@RequestBody Project project) {
    return projectService.save(project);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Project> list() {
    return projectService.list();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Project get(@PathVariable long id) {
    return projectService.get(id);
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public String handleException(UnsupportedOperationException e) {
    return ObjectUtils.defaultIfNull(e.getLocalizedMessage(), e.getMessage());
  }

  @RequestMapping(value = "/github", method = RequestMethod.GET)
  public List<?> getFromGitHub() throws IOException {
    return projectService.getFromGitHub();
  }
}
