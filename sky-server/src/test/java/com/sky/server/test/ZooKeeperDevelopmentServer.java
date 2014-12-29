package com.sky.server.test;

import org.apache.curator.test.TestingServer;

import java.io.IOException;

/**
 * Created by JCooky on 14. 12. 28..
 */
public class ZooKeeperDevelopmentServer {
  public static void main(String []args) throws Exception {
    final TestingServer server = new TestingServer(32181, false);

    server.start();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          server.stop();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
