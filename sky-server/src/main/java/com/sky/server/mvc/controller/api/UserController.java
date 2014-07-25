package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.Project;
import com.sky.server.mvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by jcooky on 2014. 7. 23..
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @RequestMapping("/{id}/projects")
  public Collection<Project> getProjects(@PathVariable Long id) {
    return userRepository.findOne(id).getProjects();
  }
}
