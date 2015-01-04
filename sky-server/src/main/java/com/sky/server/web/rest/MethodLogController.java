package com.sky.server.web.rest;

import com.sky.commons.domain.ClassKey;
import com.sky.commons.domain.MethodKey;
import com.sky.commons.domain.MethodLog;
import com.sky.server.domain.ClassKeyRepository;
import com.sky.server.domain.MethodKeyRepository;
import com.sky.server.domain.MethodLogRepository;
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