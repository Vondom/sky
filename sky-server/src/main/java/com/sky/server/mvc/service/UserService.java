package com.sky.server.mvc.service;

import com.sky.server.mvc.model.User;
import com.sky.server.mvc.repository.UserRepository;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * Created by jcooky on 2014. 7. 21..
 */
@Service
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private Connection<?> connection;

  @Autowired
  private GitHubClient ghClient;

  public User getMe() {
    return userRepository.findByEmail(connection.fetchUserProfile().getEmail());
  }

  public User get(long id) {
    return userRepository.findOne(id);
  }

  @Transactional(readOnly = true)
  public User createNotExists(Connection<?> connection) {
    UserProfile userProfile = connection.fetchUserProfile();
    String email = userProfile.getEmail();

    User user = userRepository.findByEmail(email);
    logger.debug("findByEmail: {}", user);
    return user != null ? user : this.create(userProfile);
  }

  @Transactional
  private User create(UserProfile userProfile) {

    User user = new User();
    user.setEmail(userProfile.getEmail());

    return userRepository.save(user);
  }

  public Map<String, List<Repository>> getGitHubRepositories() throws IOException {
    Map<String, List<Repository>> repositories = new HashMap<String, List<Repository>>();

    org.eclipse.egit.github.core.service.UserService userService = new org.eclipse.egit.github.core.service.UserService(ghClient);
    String userName = userService.getUser().getName();

    RepositoryService service = new RepositoryService(ghClient);
    repositories.put(userName, new ArrayList<Repository>());
    for (Repository repo : service.getRepositories(userName)) {
      repositories.get(userName).add(repo);
    }

    OrganizationService organizationService = new OrganizationService(ghClient);
    for (org.eclipse.egit.github.core.User user : organizationService.getOrganizations(userName)) {
      String orgName = user.getLogin();
      repositories.put(orgName, new ArrayList<Repository>());

      for (Repository repo : service.getOrgRepositories(orgName)) {
        repositories.get(orgName).add(repo);
      }
    }

    return repositories;
  }


}
