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

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Worker implements com.sky.commons.Worker.Iface {
  private static final Logger logger = LoggerFactory.getLogger(Worker.class);

  @Autowired
  private Processor processor;

  private long id;

  private final Status status = new Status()
      .setState(State.IDLE);

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
      IOUtils.copy(process.getInputStream(), System.out);
      IOUtils.copy(process.getErrorStream(), System.err);
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

      workerControlService.done(id, work.id);
    }
  }

  private File setup(Jar jar) throws IOException {
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

  public void setWorkerControlService(WorkerControlService.Iface workerControlService) {
    this.workerControlService = workerControlService;
  }
}
