package com.sky.server.mvc.service;

import com.sky.server.mvc.model.Project;
import com.sky.server.mvc.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jcooky on 2014. 7. 28..
 */
@Service
public class ProjectService {
  @Autowired
  private UserService userService;

  @Autowired
  private ProjectRepository projectRepository;

  public Project save(Project project) {
    project.setOwner(userService.getMe());

    return projectRepository.save(project);
  }

  public List<Project> list() {
    return projectRepository.findAll();
  }

  public Project get(long id) {
    return projectRepository.findOne(id);
  }
}
