package com.sky.worker;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by jcooky on 2014. 7. 31..
 */
@Component
public class Options {

  @Resource
  private Environment env;

  public String get(Key key) {
    return env.getProperty(key.getKey());
  }

  public static enum Key {
    SERVER_URL("worker.options.server.url", "Sky Server Host URL"),
//    NAME("worker.options.name", "Worker name"),
    PORT("worker.options.local.port", "Worker port"),
    LOCAL_HOST("worker.options.local.host", "Worker local host")
    ;

    private String key, description;

    private Key(String key, String description) {
      this.key = key;
      this.description = description;
    }

    public String getKey() {
      return key;
    }

    public String getDescription() {
      return description;
    }
  }
}
