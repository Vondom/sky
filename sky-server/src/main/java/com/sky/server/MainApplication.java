package com.sky.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
    SpringApplication.run(MainApplication.class);
  }
}
