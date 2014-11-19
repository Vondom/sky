package com.sky.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jcooky on 2014. 9. 6..
 */
@Component
public class Processor {

  @Autowired
  private Options options;

  public Process process(long workId, String path, String arguments) throws IOException {
    List<String> processes = Arrays.asList("java",
        String.format("-Dsky.profiler.config=http://%s/download/config/%d", options.get(Options.Key.HOST), workId),
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
        String.format("-Dsky.profiler.config=http://%s/download/config/%d", options.get(Options.Key.HOST), workId),
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
