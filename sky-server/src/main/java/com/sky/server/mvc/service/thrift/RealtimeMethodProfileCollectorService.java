package com.sky.server.mvc.service.thrift;

import com.sky.commons.AgentControlService;
import com.sky.commons.MethodProfile;
import com.sky.server.config.annotation.TService;
import com.sky.server.mvc.model.MethodLog;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.repository.MethodLogRepository;
import com.sky.server.mvc.service.MethodKeyService;
import com.sky.server.mvc.service.WorkService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jcooky on 2014. 7. 26..
 */
@TService(name = "profile", thrift = AgentControlService.class)
public class RealtimeMethodProfileCollectorService implements AgentControlService.Iface {
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RealtimeMethodProfileCollectorService.class);

  @Autowired
  private MethodLogRepository methodLogRepository;

  @Autowired
  private MethodKeyService methodKeyService;

  @Autowired
  private WorkService workService;


  @Transactional
  public void put(MethodProfile profile) {
    MethodLog methodLog = new MethodLog();
    methodLog.setMethodKey(methodKeyService.get(profile.getCallee()));
    methodLog.setCaller(profile.getCaller() == null ? null : methodKeyService.get(profile.getCaller()));
    methodLog.setElapsedTime(profile.getElapsedTime());
    methodLog.setStartTime(profile.getTimestamp());
    methodLog.setOrdering(profile.getIndex());
    methodLog.setThreadName(profile.getThreadName());

    methodLog = methodLogRepository.save(methodLog);
    Work work = workService.get(profile.getProfileId());

    work.getMethodLogs().add(methodLog);
    workService.save(work);
  }

  @Transactional
  public long createProfile(long workId) {
//    Profile profile = new Profile();
////    Work work = ;
//
//    profile.setWork(workService.get(workId));
//
//    profile = profileService.create(profile);
//
//    logger.trace("profileId: {}", profile);

    return workId;
  }
}
