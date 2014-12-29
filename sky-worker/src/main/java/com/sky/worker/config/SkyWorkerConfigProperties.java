package com.sky.worker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Created by JCooky on 14. 12. 29..
 */
@Component
@ConfigurationProperties(prefix = "sky.worker")
public class SkyWorkerConfigProperties {
  @NotNull
  private String serverConnection;

  public String getServerConnection() {
    return serverConnection;
  }

  public void setServerConnection(String serverConnection) {
    this.serverConnection = serverConnection;
  }
}
