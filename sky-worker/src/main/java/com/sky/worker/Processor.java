package com.sky.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jcooky on 2014. 9. 6..
 */
@Component
public class Processor {

  @Autowired
  private Options options;

  public Process process(long projectId, long workId, String path) throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder(
        "java",
        String.format("-Dsky.profiler.config=http://%s/download/config/%d/%d", options.get(Options.Key.HOST), projectId, workId),
        "-javaagent:"+Worker.PROFILER_PATH,
        "-jar",
        path
    );

    return processBuilder.start();
  }
}
