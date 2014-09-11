package com.sky.worker;

import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by jcooky on 2014. 9. 6..
 */
@Service
public class Processor {

  private int port = 8080;

  public Process process(long projectId, long workId, String path) throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder(
        "java",
        String.format("-Dsky.profiler.config=http://localhost:%d/download/config/%d/%d", port, projectId, workId),
        "-javaagent:"+Worker.PROFILER_PATH,
        "-jar",
        path
    );

    return processBuilder.start();
  }
}
