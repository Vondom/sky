package com.sky.server;

import org.springframework.boot.actuate.system.ApplicationPidListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jcooky on 2014. 6. 16..
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class MainApplication {
  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(MainApplication.class)
        .showBanner(false)
        .addCommandLineProperties(true)
        .listeners(new ApplicationPidListener("sky-server.pid"))
        .run(args);

  }
}
