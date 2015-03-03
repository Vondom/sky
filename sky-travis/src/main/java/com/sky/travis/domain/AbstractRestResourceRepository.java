package com.sky.travis.domain;

import com.sky.travis.config.SkyTravisConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by JCooky on 14. 12. 29..
 */
public abstract class AbstractRestResourceRepository {
  @Autowired
  private SkyTravisConfigProperties properties;

  protected String path(String path) {
    return properties.getServerUrl() + requestPath() + path;
  }

  protected abstract String requestPath();
}
