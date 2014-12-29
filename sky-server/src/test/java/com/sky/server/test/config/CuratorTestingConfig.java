package com.sky.server.test.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by JCooky on 14. 12. 29..
 */
@Configuration
@Profile("test")
public class CuratorTestingConfig {
  @Bean(initMethod = "start", destroyMethod = "close")
  public TestingServer testingServer() throws Exception {
    return new TestingServer(false);
  }

  @Bean(initMethod = "start", destroyMethod = "close")
  public CuratorFramework curatorFramework(TestingServer testingServer) {
    return CuratorFrameworkFactory.newClient(testingServer.getConnectString(), new RetryOneTime(100));
  }
}
