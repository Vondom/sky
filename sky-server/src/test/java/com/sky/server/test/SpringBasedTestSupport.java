package com.sky.server.test;

import com.sky.server.SkyServer;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
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
@WebAppConfiguration
@SpringApplicationConfiguration(classes = SkyServer.class)
@IntegrationTest({"server.port=0", "management.port=0"})
public abstract class SpringBasedTestSupport {

  @Autowired
  protected EntityManager em;

}
