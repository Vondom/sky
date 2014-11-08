package com.sky.server.mvc.service;

import com.sky.server.mvc.model.Project;
import com.sky.server.mvc.model.User;
import com.sky.server.mvc.repository.ProjectRepository;
import com.sky.server.mvc.repository.UserRepository;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
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

  @Autowired
  private GitHubClient gitHubClient;

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

  public List<?> getFromGitHub() throws IOException {
    RepositoryService repositoryService = new RepositoryService(gitHubClient);
    return repositoryService.getRepositories();
  }

  public Project createFromGitHub(Repository repo) {
    Project project = new Project();
    project.setName(repo.getName());
    project.setDescription(repo.getDescription());
    project.setOwner(userService.getMe());

    return projectRepository.save(project);
  }

  @Transactional
  public void delete(long id) {
    projectRepository.delete(id);
  }
}
