package com.sky.worker;

import com.sky.commons.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Worker implements com.sky.commons.Worker.Iface {
  private static final Logger logger = LoggerFactory.getLogger(Worker.class);

  public static final String PROFILER_PATH = FileUtils.getTempDirectoryPath() + "/sky-profiler.jar";

  @Autowired
  private Processor processor;

  @Autowired
  private Options options;

  private long id;

  private final Status status = new Status()
      .setState(State.IDLE);

  private final String profilerPath = "/sky-profiler.jar";

  private WorkerControlService.Iface workerControlService;

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  @Override
  public String doWork(Work work) throws TException {

    File file = null;
    try {

      file = setup(work.getJar());
      status.setState(State.WORKING);

      Process process = processor.process(work.id, file.getAbsolutePath(), work.getArguments());
//      IOUtils.copy(process.getErrorStream(), System.err);
      IOUtils.copy(process.getInputStream(), System.out);
      int exitCode = process.waitFor();
      logger.debug("exitCode: {}", exitCode);

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

      workerControlService.done(id);
    }
  }

  private File setup(Jar jar) throws IOException {
    String host = options.get(Options.Key.HOST);
    File profilerFile = new File(PROFILER_PATH);
    if (!profilerFile.exists()) {
      FileUtils.copyURLToFile(getProfilerUrl(host), profilerFile);
      logger.debug("FINISH Download profiler: {}", profilerFile.getAbsolutePath());
    }

    File jarfile = new File(FileUtils.getTempDirectoryPath() + "/" + jar.getName());
    FileUtils.writeByteArrayToFile(jarfile, jar.getFile());

    return jarfile;
  }

  public URL getProfilerUrl(String host) throws MalformedURLException {
    return new URL("http://" + host + profilerPath);
  }

  @Override
  public Status status() throws TException {
    synchronized (status) {
      return status;
    }
  }

  public void setWorkerControlService(WorkerControlService.Iface workerControlService) {
    this.workerControlService = workerControlService;
  }
}
