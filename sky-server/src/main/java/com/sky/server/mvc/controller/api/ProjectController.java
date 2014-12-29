package com.sky.server.mvc.controller.api;

import com.sky.commons.model.ExecutionUnit;
import com.sky.commons.model.Project;
import com.sky.server.mvc.repository.ProjectRepository;
import com.sky.server.service.GitHubHelper;
import com.sky.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jcooky on 2014. 7. 25..
 */
@RestController
@RequestMapping(value = "/api/project")
public class ProjectController {
  private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

  @Resource
  private ProjectRepository projectRepository;

  @Resource
  private UserService userService;

  @Resource
  private GitHubHelper gitHubHelper;


  @RequestMapping(method = RequestMethod.POST)
  public Project create(@RequestBody Project project) {
    project.setOwner(userService.me());
    return projectRepository.save(project);
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Project> list() {
    return projectRepository.findAll();
  }

  @RequestMapping(value = "/{id}/execution-units", method = RequestMethod.GET)
  public List<ExecutionUnit> getExecutionUnits(@PathVariable long id) {
    return get(id).getExecutionUnits();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Project get(@PathVariable long id) {
    return projectRepository.findOne(id);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public boolean delete(@PathVariable long id) {
    projectRepository.delete(id);
    return true;
  }

}
