package com.sky.server.mvc.repository;

import com.sky.commons.domain.ClassKey;
import com.sky.commons.domain.MethodKey;
import com.sky.server.domain.ClassKeyRepository;
import com.sky.server.domain.MethodKeyRepository;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MethodKeyRepositoryTest extends SpringBasedTestSupport {

  @Autowired
  protected ClassKeyRepository classKeyRepository;

  @Autowired
  protected MethodKeyRepository methodKeyRepository;

  @Test
  public void testFindOne() throws Exception {
    ClassKey classKey = new ClassKey();
    classKey.setPackageName("org.test");
    classKey.setName("TesterClass");
    classKey = classKeyRepository.save(classKey);

    MethodKey methodKey = new MethodKey();
    methodKey.setSignature("V(Ljava/lang/Object;)");
    methodKey.setName("test");
    methodKey.setClassKey(classKey);
    methodKey = methodKeyRepository.save(methodKey);

    MethodKey methodKey_ = methodKeyRepository.findBySignatureAndNameAndClassKey(methodKey.getSignature(),
        methodKey.getName(), classKey);

    assertThat(methodKey_, is(methodKey));
  }
}