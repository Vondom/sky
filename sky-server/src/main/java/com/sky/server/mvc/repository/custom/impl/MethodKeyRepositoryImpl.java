package com.sky.server.mvc.repository.custom.impl;

import com.mysema.query.jpa.impl.JPAQuery;
import com.sky.server.mvc.model.MethodKey;
import com.sky.server.mvc.model.QClassKey;
import com.sky.server.mvc.model.QMethodKey;
import com.sky.server.mvc.repository.custom.MethodKeyRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

/**
 * Created by jcooky on 2014. 7. 9..
 */
public class MethodKeyRepositoryImpl implements MethodKeyRepositoryCustom {
  @Autowired
  private EntityManager em;

  @Override
  public MethodKey findOne(String signature, String name, String className, String packageName) {
    QMethodKey methodKey = new QMethodKey("methodKey");
    QClassKey classKey = new QClassKey("classKey");

    JPAQuery query = new JPAQuery(em);
    return query.from(methodKey, classKey)
        .where(methodKey.signature.eq(signature), methodKey.name.eq(name), methodKey.classKey.eq(classKey),
            classKey.name.eq(className), classKey.packageName.eq(packageName))
        .uniqueResult(methodKey);
  }
}
