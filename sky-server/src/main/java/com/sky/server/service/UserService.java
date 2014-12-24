package com.sky.server.service;

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

  public User me() {
    return userRepository.findByEmail(connection.fetchUserProfile().getEmail());
  }



}
