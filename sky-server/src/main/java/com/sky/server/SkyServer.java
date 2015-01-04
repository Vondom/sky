package com.sky.server;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.apache.curator.retry.RetryOneTime;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by jcooky on 2014. 6. 16..
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.sky.server", "com.sky.commons"})
@EntityScan({"com.sky.server", "com.sky.commons"})
@EnableScheduling
@EnableAsync
public class SkyServer {

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

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(SkyServer.class)
        .showBanner(true)
        .addCommandLineProperties(false)
        .listeners(new ApplicationPidFileWriter("sky-server.pid"))
        .run(args);
  }
}
