package com.sky.travis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Created by JCooky on 15. 1. 24..
 */
@Component
@ConfigurationProperties(prefix = "sky.travis")
public class SkyTravisConfigProperties {

  @NotNull
  private String serverUrl;

  public String getServerUrl() {
    return serverUrl;
  }

  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }
}
