package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.Project;
import com.sky.server.mvc.service.UserService;
import org.eclipse.egit.github.core.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by jcooky on 2014. 7. 23..
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserService userService;

  @RequestMapping("/{id}/projects")
  public Collection<Project> getProjects(@PathVariable long id) {
    return userService.get(id).getProjects();
  }

  @RequestMapping("/{id}/github/repositories")
  public Map<String, List<Repository>> getRepositories(@PathVariable long id) throws IOException {
    return userService.getGitHubRepositories();
  }
}
