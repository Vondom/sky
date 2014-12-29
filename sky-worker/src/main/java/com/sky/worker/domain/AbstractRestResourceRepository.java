package com.sky.worker.domain;

import com.sky.worker.config.SkyWorkerConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * Created by JCooky on 14. 12. 29..
 */
public abstract class AbstractRestResourceRepository {
  @Autowired
  private SkyWorkerConfigProperties properties;

  @Autowired
  protected RestTemplate restTemplate;

  protected String path(String path) {
    return properties.getServerConnection() + requestPath() + path;
  }

  protected abstract String requestPath();
}
