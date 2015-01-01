package com.sky.server.mvc.controller.api;

import com.sky.commons.model.ClassKey;
import com.sky.commons.model.MethodKey;
import com.sky.commons.model.MethodLog;
import com.sky.server.mvc.repository.ClassKeyRepository;
import com.sky.server.mvc.repository.MethodKeyRepository;
import com.sky.server.mvc.repository.MethodLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jcooky on 2014. 7. 13..
 */
@RestController
@RequestMapping("/api/method-log")
public class MethodLogController {

  @Autowired
  private MethodLogRepository methodLogRepository;

  @Autowired
  private MethodKeyRepository methodKeyRepository;

  @Autowired
  private ClassKeyRepository classKeyRepository;

  @RequestMapping(method = RequestMethod.POST)
  @Transactional
  public MethodLog create(@RequestBody MethodLog methodLog) {
    methodLog.setCaller(saveMethodKey(methodLog.getCaller()));
    methodLog.setMethodKey(saveMethodKey(methodLog.getMethodKey()));

    return methodLogRepository.save(methodLog);
  }

  private MethodKey saveMethodKey(MethodKey methodKey) {
    if (methodKey != null && !methodKeyRepository.exists(methodKey.getId())) {
      methodKey.setClassKey(saveClassKey(methodKey.getClassKey()));
      methodKey = methodKeyRepository.save(methodKey);
    }

    return methodKey;
  }

  private ClassKey saveClassKey(ClassKey classKey) {
    if (!classKeyRepository.exists(classKey.getId())) {
      classKey = classKeyRepository.save(classKey);
    }

    return classKey;
  }
}

/*
null
0
1419996714581
-1
main
MethodKey{id=0, name='acquire', signature='()V'}
  SimpleSemaphore
  com.sky.sample
MethodKey{id=0, name='process', signature='(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V'}
  PingPongRight
  com.sky.sample
0
null
 */