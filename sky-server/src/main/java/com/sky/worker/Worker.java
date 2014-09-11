package com.sky.worker;

import com.sky.commons.*;
import com.sky.server.mvc.service.WorkService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class Worker implements com.sky.commons.Worker.Iface {
  private static final Logger logger = LoggerFactory.getLogger(Worker.class);

  public static final String PROFILER_PATH = FileUtils.getTempDirectoryPath() + "sky-profiler.jar";

  @Autowired
  private Processor processor;

  private Status status = new Status()
      .setState(State.IDLE);

  @Autowired
  private WorkService workService;

  private String profilerPath = "/sky-profiler.jar";

  @Override
  @Async
  public String doWork(Work work) throws TException {

    File file = null;
    try {

      file = setup(work.getJar());
      status.setState(State.WORKING);

      Process process = processor.process(work.projectId, work.id, file.getAbsolutePath());
      int exitCode = process.waitFor();
      logger.debug("exitCode: {}", exitCode);
      logger.error(IOUtils.toString(process.getErrorStream()));
      InputStream is = process.getInputStream();
      IOUtils.copy(is, System.out);

      return "";
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      throw new WorkingException();
    } catch (InterruptedException e) {
      logger.error(e.getMessage(), e);
      throw new WorkingException();
    } finally {
      if (file == null || !file.delete()) {
        String path = file != null ? file.getAbsolutePath() : "";
        logger.error("FAILED file deletion: {}", path);
      }
      status.setState(State.IDLE);

      workService.done(work);
    }
  }

  private File setup(Jar jar) throws IOException {
    File profilerFile = new File(PROFILER_PATH);
    if (!profilerFile.exists()) {
      FileUtils.copyURLToFile(new URL("http://localhost:8080/" + profilerPath), profilerFile);
      logger.debug("FINISH Donload profiler: {}", profilerFile.getAbsolutePath());
    }

    File jarfile = new File(FileUtils.getTempDirectoryPath() + "/" + jar.getName());
    FileUtils.writeByteArrayToFile(jarfile, jar.getFile());

    return jarfile;
  }

  @Override
  public Status status() throws TException {
    synchronized (status) {
      return status;
    }
  }
}
