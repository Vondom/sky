package com.sky.server.mvc.repository;

import com.sky.server.mvc.model.MethodKey;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MethodKeyRepositoryTest extends SpringBasedTestSupport {
  @Test
  public void testFindOne() throws Exception {
    MethodKey methodKey = methodKeyRepository.findBySignatureAndNameAndClassKey(super.mockMethodKey.getSignature(),
        super.mockMethodKey.getName(), mockClassKey);

    assertEquals(mockMethodKey, methodKey);
  }
}