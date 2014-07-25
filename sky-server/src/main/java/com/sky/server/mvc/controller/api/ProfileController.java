package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.MethodLog;
import com.sky.server.mvc.model.Profile;
import com.sky.server.mvc.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by jcooky on 2014. 7. 10..
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

  @Autowired
  private ProfileRepository profileRepository;

  @RequestMapping(method = RequestMethod.GET)
  @Transactional(readOnly = true)
  public List<Profile> get() {
    return profileRepository.findAll();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @Transactional(readOnly = true)
  public Profile get(@PathVariable Long id) {
    return profileRepository.findOne(id);
  }

  @RequestMapping(value = "/{id}/methodLogs", method = RequestMethod.GET)
  @Transactional(readOnly = true)
  public Collection<MethodLog> getMethodLogs(@PathVariable Long id) {
    return profileRepository.findOne(id).getMethodLogs();
  }

  @RequestMapping(method = RequestMethod.POST)
  @Transactional
  public Profile create(@RequestBody(required = false) Profile profile) {
    return profileRepository.save(profile);
  }
}
