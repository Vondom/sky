package com.sky.server.mvc.service;

import com.sky.server.mvc.model.User;
import com.sky.server.mvc.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.github.api.GitHub;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  public User getMe() {
    return userRepository.findByEmail(connection.fetchUserProfile().getEmail());
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

}
