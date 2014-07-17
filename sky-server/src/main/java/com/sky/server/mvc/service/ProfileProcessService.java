package com.sky.server.mvc.service;

import com.sky.commons.RealtimeMethodProfile;
import com.sky.server.mvc.model.MethodLog;
import com.sky.server.mvc.model.Profile;
import com.sky.server.mvc.repository.MethodLogRepository;
import com.sky.server.mvc.repository.ProfileRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jcooky on 2014. 7. 7..
 */
@Service
public class ProfileProcessService {

  @Autowired
  private ProfileRepository repository;

  @Autowired
  private MethodLogRepository methodLogRepository;

  @Autowired
  private MethodKeyService methodKeyService;

  @Transactional
  public void put(RealtimeMethodProfile profile) {
    MethodLog methodLog = new MethodLog();
    methodLog.setMethodKey(methodKeyService.get(profile.getMethod()));
    methodLog.setCaller(ObjectUtils.defaultIfNull(methodKeyService.get(profile.getCaller()), null));
    methodLog.setElapsedTime(profile.getElapsedTime());
    methodLog.setStartTime(profile.getTimestamp());
    methodLog.setOrdering(profile.getIndex());
    methodLog.setThreadName(profile.getThreadName());

    methodLog = methodLogRepository.save(methodLog);
    Profile profile1 = repository.findOne(profile.getProfileId());

    profile1.getMethodLogs().add(methodLog);
    repository.save(profile1);
  }

  @Transactional
  public long create(Profile profile) {
    profile = repository.save(profile);
    return profile.getId();
  }
}
