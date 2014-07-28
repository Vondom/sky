package com.sky.server.mvc.repository.custom;

import com.sky.server.mvc.model.ClassKey;
import com.sky.server.mvc.model.MethodKey;

/**
 * Created by jcooky on 2014. 7. 9..
 */
public interface MethodKeyRepositoryCustom {
  public MethodKey findOne(String signature, String name, ClassKey classKey);
}
