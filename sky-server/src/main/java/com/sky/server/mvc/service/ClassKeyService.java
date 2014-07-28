package com.sky.server.mvc.service;

import com.sky.commons.KClass;
import com.sky.server.mvc.model.ClassKey;
import com.sky.server.mvc.repository.ClassKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jcooky on 2014. 7. 28..
 */
@Service
public class ClassKeyService {

  @Autowired
  private ClassKeyRepository classKeyRepository;

  @Transactional(readOnly = true)
  public ClassKey get(KClass classKey) {
    ClassKey ck = classKeyRepository.findByNameAndPackageName(classKey.getClassName(), classKey.getPackageName());

    if (ck == null) {
      ck = create(classKey.getPackageName(), classKey.getClassName());
    }

    return ck;
  }

  @Transactional
  private ClassKey create(String packageName, String className) {
    ClassKey ck = new ClassKey();
    ck.setPackageName(packageName);
    ck.setName(className);

    return classKeyRepository.save(ck);
  }
}
