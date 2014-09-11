package com.sky.worker.service;

import com.sky.commons.*;
import com.sky.worker.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
* Created by jcooky on 2014. 7. 30..
*/
public class Worker implements com.sky.commons.Worker.Iface {
  private static final Logger logger = LoggerFactory.getLogger(Worker.class);

  private Runner runner;

  private WorkerControlService.Iface workerControlService;

  private Options options;

  private Status status = new Status()
      .setState(State.IDLE);

  public Worker(Runner runner, WorkerControlService.Iface workerControlService, Options options) {
    this.runner = runner;
    this.workerControlService = workerControlService;
    this.options = options;
  }

  public void init() throws TException {
    int port = Integer.parseInt(options.get(Options.Key.PORT));

    status.setWorkerId(workerControlService.add("0.0.0.0", port));
  }

  @Override
  public String doWork(Work work) throws TException {

    try {
      synchronized(status) {
        status.setState(State.WORKING);

        Process process = runner.run(work.projectId, setup(work.getJar()).getAbsolutePath());
        int exitCode = process.waitFor();
        logger.debug("exitCode: {}", exitCode);
        logger.error(IOUtils.toString(process.getErrorStream()));

        return IOUtils.toString(process.getInputStream());
      }
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      throw new WorkingException();
    } catch (InterruptedException e) {
      logger.error(e.getMessage(), e);
      throw new WorkingException();
    } finally {
      status.setState(State.IDLE);
    }
  }

  private File setup(Jar jar) throws IOException {
    File jarfile = new File(FileUtils.getTempDirectoryPath() + "/" + jar.getName());
    FileUtils.writeByteArrayToFile(jarfile, jar.getFile());

    return jarfile;
  }

  @Override
  public Status status() throws TException {
    synchronized(status) {
      return status;
    }
  }

  public void destroy() {
    try {
      workerControlService.done(status.getWorkerId());
    } catch (TException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
