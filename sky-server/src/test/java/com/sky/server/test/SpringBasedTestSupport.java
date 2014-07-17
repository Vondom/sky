package com.sky.server.test;

import com.sky.server.MainApplication;
import com.sky.server.mvc.model.ClassKey;
import com.sky.server.mvc.model.MethodKey;
import com.sky.server.mvc.repository.ClassKeyRepository;
import com.sky.server.mvc.repository.MethodKeyRepository;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;

/**
 * Created by jcooky on 2014. 7. 9..
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("sky-server/src/main/webapp")
@SpringApplicationConfiguration(classes = MainApplication.class)
//@IntegrationTest({"server.port=0", "management.port=0"})
public abstract class SpringBasedTestSupport {

  @Autowired
  protected EntityManager em;

  @Autowired
  protected ClassKeyRepository classKeyRepository;

  @Autowired
  protected MethodKeyRepository methodKeyRepository;

  protected ClassKey mockClassKey;
  protected MethodKey mockMethodKey;

  @Rule
  public TestWatcher dbRule = new TestWatcher() {

    @Override
    protected void starting(Description description) {

      ClassKey classKey = new ClassKey();
      classKey.setPackageName("org.test");
      classKey.setName("TesterClass");
      classKey = classKeyRepository.save(classKey);

      MethodKey methodKey = new MethodKey();
      methodKey.setSignature("V(Ljava/lang/Object;)");
      methodKey.setName("test");
      methodKey.setClassKey(classKey);
      methodKey = methodKeyRepository.save(methodKey);

      mockClassKey = classKey;
      mockMethodKey = methodKey;
    }

    @Override
    protected void finished(Description description) {
      methodKeyRepository.deleteAll();
      classKeyRepository.deleteAll();

      mockClassKey = null;
      mockMethodKey = null;
    }


  };
}
