package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.Project;
import com.sky.server.mvc.model.User;
import com.sky.server.mvc.repository.UserRepository;
import com.sky.server.service.UserService;
import org.eclipse.egit.github.core.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
  private UserRepository userRepository;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public User getUser(@PathVariable long id) {
    return userRepository.findOne(id);
  }

  @RequestMapping(value = "/{id}/projects", method = RequestMethod.GET)
  public Collection<Project> getProjects(@PathVariable long id) {
    return userRepository.findOne(id).getProjects();
  }
}
