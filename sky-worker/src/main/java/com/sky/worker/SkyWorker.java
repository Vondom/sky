package com.sky.worker;

import com.sky.worker.domain.WorkerRepository;
import com.sky.worker.service.TaskDistributedQueue;
import com.sky.worker.service.Worker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * Created by jcooky on 2014. 7. 30..
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.sky.worker"})
public class SkyWorker implements CommandLineRunner {
  private static final Logger logger = LoggerFactory.getLogger(SkyWorker.class);
  public static final String PROFILER_PATH = FileUtils.getTempDirectoryPath() + "/sky-profiler.jar";

  @Autowired
  private Environment env;

  @Autowired
  private Worker worker;

  @Autowired
  private WorkerRepository workerRepository;

  @Autowired
  private TaskDistributedQueue taskDistributedQueue;


  private boolean startLoop = false;

  public void run(String[] args) throws Exception {

    if (!ArrayUtils.contains(env.getActiveProfiles(), "test")) {
      // setup
      this.setUpProfiler();

      // Register self worker
      this.startWorker();

      try {
        // Run worker
        this.runWorker();
      } finally {
        // Remove self from database
        this.shutdownWorker();
      }
    }
  }

  private void shutdownWorker() {
    workerRepository.delete(this.worker.getId());
    this.startLoop = false;
  }

  private void runWorker() throws Exception {
    while (this.startLoop) {
      long workId = taskDistributedQueue.take();

      this.worker.doWork(workId);
    }
  }

  private void startWorker() {
    com.sky.commons.domain.Worker worker = new com.sky.commons.domain.Worker();
    worker = workerRepository.save(worker);
    this.worker.setId(worker.getId());
    this.startLoop = true;
  }

  public void setUpProfiler() throws IOException {
    File profilerFile = new File(PROFILER_PATH);

    FileUtils.copyInputStreamToFile(new ClassPathResource("/sky-profiler.jar").getInputStream(), profilerFile);
    logger.debug("FINISH Download profiler: {}", profilerFile.getAbsolutePath());
  }

  public static void main(String[] args) throws Exception {
    new SpringApplicationBuilder()
        .showBanner(true)
        .web(false)
        .addCommandLineProperties(false)
        .sources(SkyWorker.class)
        .listeners(new ApplicationPidFileWriter("sky-worker.pid"))
        .run(args);
  }
}
