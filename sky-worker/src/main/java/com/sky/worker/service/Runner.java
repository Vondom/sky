package com.sky.worker.service;

import com.sky.worker.Options;
import com.sky.worker.config.WorkerConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
* Created by jcooky on 2014. 7. 30..
*/
public class Runner {
  private static final Logger logger = LoggerFactory.getLogger(Runner.class);

  private Options options;

  public Runner(Options options) {
    this.options = options;
  }

  public Process run(long projectId, String jarpath) throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder(
        "java",
        StringUtils.join("-Dsky.profiler.config=\"", options.get(Options.Key.HOST), "/config/", projectId, "\""),
        StringUtils.join("-javaagent:", WorkerConfig.SKY_PROFILER_JAR_PATH),
        "-jar",
        jarpath
    );

    return processBuilder.start();
  }
}
