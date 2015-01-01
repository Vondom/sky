package com.sky.worker.service;

import com.sky.worker.SkyWorker;
import com.sky.worker.config.SkyWorkerConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jcooky on 2014. 9. 6..
 */
@Service
public class Processor {
  @Autowired
  private SkyWorkerConfigProperties properties;

  public Process process(long workId, String path, String arguments) throws IOException {
    List<String> processes = Arrays.asList("java",
        "-Dsky.profiler.server-connection="+properties.getServerConnection(),
        String.format("-Dsky.profiler.config=%s/download/config/%d", properties.getServerConnection(), workId),
        "-javaagent:"+ SkyWorker.PROFILER_PATH,
        "-jar",
        path);
    if (arguments != null && !arguments.isEmpty())
      processes.add(arguments);

    ProcessBuilder processBuilder = new ProcessBuilder(processes.toArray(new String[processes.size()]));

    return processBuilder.start();
  }

  public Process process(long workId, String path, String mainClassName, String arguments) throws IOException {
    List<String> processes = Arrays.asList("java",
        "-Dsky.profiler.server-connection="+properties.getServerConnection(),
        String.format("-Dsky.profiler.config=%s/download/config/%d", properties.getServerConnection(), workId),
        "-javaagent:"+ SkyWorker.PROFILER_PATH,
        "-cp",
        path,
        mainClassName);
    if (arguments != null && !arguments.isEmpty())
      processes.add(arguments);

    ProcessBuilder processBuilder = new ProcessBuilder(processes.toArray(new String[processes.size()]));

    return processBuilder.start();
  }
}
