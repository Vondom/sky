package com.sky.server.mvc.service;

import com.sky.server.mvc.model.ClassKey;
import com.sky.server.mvc.model.MethodKey;
import com.sky.server.mvc.repository.ClassKeyRepository;
import com.sky.server.mvc.repository.MethodKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jcooky on 2014. 7. 9..
 */
@Service
public class MethodKeyService {

  @Autowired
  private MethodKeyRepository methodKeyRepository;

  @Autowired
  private ClassKeyRepository classKeyRepository;

  @Transactional(readOnly = true)
  public MethodKey get(final org.shiftone.jrat.core.MethodKey methodKey) {
    MethodKey mk = methodKeyRepository.findOne(methodKey.getSignature(), methodKey.getMethodName(), methodKey.getClassName(), methodKey.getPackageName());

    if (mk == null) {
      mk = create(methodKey.getSignature(), methodKey.getMethodName(), methodKey.getClassName(), methodKey.getPackageName());
    }

    return mk;
  }

  @Transactional
  public MethodKey create(String signature, String methodName, String className, String packageName) {
    ClassKey ck = classKeyRepository.findByNameAndPackageName(className, packageName);
    if (ck == null) {
      ck = new ClassKey();
      ck.setName(className);
      ck.setPackageName(packageName);
      ck = classKeyRepository.save(ck);
    }

    MethodKey mk = new MethodKey();
    mk.setName(methodName);
    mk.setSignature(signature);
    mk.setClassKey(ck);
    methodKeyRepository.save(mk);

    return mk;
  }
}
