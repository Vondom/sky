package com.sky.server.mvc.service;

import com.sky.server.mvc.model.Profile;
import com.sky.server.mvc.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jcooky on 2014. 9. 11..
 */
@Service
public class ProfileService {

  @Autowired
  private ProfileRepository profileRepository;

  public Profile get(long id) {
    return profileRepository.findOne(id);
  }

  @Transactional
  public Profile save(Profile profile) {
    return profileRepository.save(profile);
  }
}
