package com.sky.server.domain.custom;

import com.sky.commons.domain.ClassKey;
import com.sky.commons.domain.MethodKey;

/**
 * Created by jcooky on 2014. 7. 9..
 */
public interface MethodKeyRepositoryCustom {
  public MethodKey findBySignatureAndNameAndClassKey(String signature, String name, ClassKey classKey);
}
