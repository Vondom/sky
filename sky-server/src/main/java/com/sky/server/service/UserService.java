package com.sky.server.service;

import com.sky.commons.domain.User;
import com.sky.server.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Service;

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
