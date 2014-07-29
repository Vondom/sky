package com.sky.server.mvc.service;

import com.sky.commons.KMethod;
import com.sky.server.mvc.model.ClassKey;
import com.sky.server.mvc.model.MethodKey;
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
  private ClassKeyService classKeyService;

  @Transactional(readOnly = true)
  public MethodKey get(final KMethod methodKey) {
    ClassKey classKey = classKeyService.get(methodKey.getClassKey());
    MethodKey mk = methodKeyRepository.findBySignatureAndNameAndClassKey(methodKey.getSignature(), methodKey.getMethodName(), classKey);

    if (mk == null) {
      mk = create(methodKey.getSignature(), methodKey.getMethodName(), classKey);
    }

    return mk;
  }

  @Transactional
  public MethodKey create(String signature, String methodName, ClassKey classKey) {

    MethodKey mk = new MethodKey();
    mk.setName(methodName);
    mk.setSignature(signature);
    mk.setClassKey(classKey);
    return methodKeyRepository.save(mk);
  }
}
