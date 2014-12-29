package com.sky.server.mvc.repository.custom;

import com.sky.commons.model.ClassKey;
import com.sky.commons.model.MethodKey;

/**
 * Created by jcooky on 2014. 7. 9..
 */
public interface MethodKeyRepositoryCustom {
  public MethodKey findBySignatureAndNameAndClassKey(String signature, String name, ClassKey classKey);
}
