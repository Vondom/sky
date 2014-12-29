package com.sky.server.service;

import com.sky.commons.model.Project;
import com.sky.server.mvc.repository.ProjectRepository;
import org.eclipse.egit.github.core.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by jcooky on 2014. 7. 28..
 */
@Service
public class GitHubHelper {
  @Autowired
  private UserService userService;

  @Autowired
  private ProjectRepository projectRepository;

  @Transactional
  public Project createFromGitHub(Repository repo) {
    Project project = new Project();
    project.setName(repo.getName());
    project.setDescription(repo.getDescription());
    project.setOwner(userService.me());

    return projectRepository.save(project);
  }
}
