package com.sky.server.service.thrift;

import com.sky.commons.AgentControlService;
import com.sky.commons.KClass;
import com.sky.commons.KMethod;
import com.sky.commons.MethodProfile;
import com.sky.server.config.annotation.TService;
import com.sky.commons.model.ClassKey;
import com.sky.commons.model.MethodKey;
import com.sky.commons.model.MethodLog;
import com.sky.commons.model.Work;
import com.sky.server.mvc.repository.ClassKeyRepository;
import com.sky.server.mvc.repository.MethodKeyRepository;
import com.sky.server.mvc.repository.MethodLogRepository;
import com.sky.server.mvc.repository.WorkRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jcooky on 2014. 7. 26..
 */
@TService(name = "agent-control", thrift = AgentControlService.class)
public class AgentControlServiceImpl implements AgentControlService.Iface {
  private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AgentControlServiceImpl.class);

  @Autowired
  private MethodLogRepository methodLogRepository;

  @Autowired
  private MethodKeyRepository methodKeyRepository;

  @Autowired
  private WorkRepository workRepository;

  @Autowired
  private ClassKeyRepository classKeyRepository;


  @Transactional
  public void put(MethodProfile profile) {
    MethodLog methodLog = new MethodLog();
    methodLog.setMethodKey(getMethodKey(profile.getCallee()));
    methodLog.setCaller(profile.getCaller() == null ? null : getMethodKey(profile.getCaller()));
    methodLog.setElapsedTime(profile.getElapsedTime());
//    methodLog.setTotalElapsedTime(profile.getTotalElapsedTime());
    methodLog.setStartTime(profile.getTimestamp());
    methodLog.setOrdering(profile.getIndex());
    methodLog.setThreadName(profile.getThreadName());

    methodLog = methodLogRepository.save(methodLog);
    Work work = workRepository.findOne(profile.getWorkId());

    work.getMethodLogs().add(methodLog);
    work.setAverageTime(work.getAverageTime() + methodLog.getElapsedTime());
    work.setMostLongTime(work.getMostLongTime() < methodLog.getElapsedTime() ? methodLog.getElapsedTime() : work.getMostLongTime());

    workRepository.save(work);
  }

  @Transactional(readOnly = true)
  private MethodKey getMethodKey(final KMethod methodKey) {
    ClassKey classKey = getClassKey(methodKey.getClassKey());
    MethodKey mk = methodKeyRepository.findBySignatureAndNameAndClassKey(methodKey.getSignature(), methodKey.getMethodName(), classKey);

    if (mk == null) {
      mk = createMethodKey(methodKey.getSignature(), methodKey.getMethodName(), classKey);
    }

    return mk;
  }

  @Transactional
  private MethodKey createMethodKey(String signature, String methodName, ClassKey classKey) {

    MethodKey mk = new MethodKey();
    mk.setName(methodName);
    mk.setSignature(signature);
    mk.setClassKey(classKey);
    return methodKeyRepository.save(mk);
  }

  @Transactional(readOnly = true)
  public ClassKey getClassKey(KClass classKey) {
    ClassKey ck = classKeyRepository.findByNameAndPackageName(classKey.getClassName(), classKey.getPackageName());

    if (ck == null) {
      ck = createClassKey(classKey.getPackageName(), classKey.getClassName());
    }

    return ck;
  }

  @Transactional
  private ClassKey createClassKey(String packageName, String className) {
    ClassKey ck = new ClassKey();
    ck.setPackageName(packageName);
    ck.setName(className);

    return classKeyRepository.save(ck);
  }
}
