package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.ExecutionUnit;
import com.sky.server.mvc.model.Project;
import com.sky.server.mvc.model.User;
import com.sky.server.mvc.service.ExecutionUnitService;
import com.sky.server.mvc.service.ProjectService;
import com.sky.server.mvc.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.egit.github.core.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by jcooky on 2014. 7. 25..
 */
@RestController
@RequestMapping(value = "/api/project")
public class ProjectController {
  private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

  @Autowired
  private ProjectService projectService;
  @Autowired
  private ExecutionUnitService executionUnitService;

  @RequestMapping(method = RequestMethod.POST)
  public Project create(@RequestBody Project project) {
    return projectService.save(project);
  }

  @RequestMapping(value = "/{id}/execution-units", method = RequestMethod.PUT, consumes = {"application/json"})
  public ExecutionUnit create(@PathVariable long id, @RequestBody ExecutionUnit executionUnit) {
    executionUnit.setProject(get(id));
    return executionUnitService.create(executionUnit);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Project> list() {
    return projectService.list();
  }

  @RequestMapping(value = "/{id}/execution-units", method = RequestMethod.GET)
  public List<ExecutionUnit> getExecutionUnits(@PathVariable long id) {
    return get(id).getExecutionUnits();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Project get(@PathVariable long id) {
    return projectService.get(id);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public boolean delete(@PathVariable long id) {
    projectService.delete(id);
    return true;
  }

  @RequestMapping(value = "/github", method = RequestMethod.GET)
  public List<?> getFromGitHub() throws IOException {
    return projectService.getFromGitHub();
  }

  @RequestMapping(value = "/github", method = RequestMethod.POST)
  public Project createFromGitHub(@RequestBody Repository repo) {
    return projectService.createFromGitHub(repo);
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public String handleException(UnsupportedOperationException e) {
    return ObjectUtils.defaultIfNull(e.getLocalizedMessage(), e.getMessage());
  }

}
