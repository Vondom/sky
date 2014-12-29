package com.sky.worker.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.apache.curator.retry.RetryOneTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * Created by JCooky on 14. 12. 29..
 */
@Configuration
public class CuratorConfig {
  @Bean(destroyMethod = "close", initMethod = "start")
  @Profile("!test")
  public CuratorFramework curatorFramework(Environment env) {
    return CuratorFrameworkFactory.builder()
        .namespace(env.getRequiredProperty("sky.curator.namespace"))
        .connectString(env.getRequiredProperty("sky.curator.connections"))
        .retryPolicy(new RetryOneTime(Integer.parseInt(env.getRequiredProperty("sky.curator.retryTime"))))
        .build();
  }

  @Bean
  public SimpleDistributedQueue distributedQueue(CuratorFramework curatorFramework) {
    return new SimpleDistributedQueue(curatorFramework, "/tasks");
  }
}
