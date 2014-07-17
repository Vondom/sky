package com.sky.sample;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jcooky on 2014. 6. 19..
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SkySampleApplication implements CommandLineRunner {
  public static void main(String[] args) {
    SpringApplication.run(SkySampleApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println("test");
  }
}