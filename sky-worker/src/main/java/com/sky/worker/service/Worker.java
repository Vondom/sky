package com.sky.worker.service;

import com.sky.commons.domain.ExecutionUnit;
import com.sky.commons.domain.MethodLog;
import com.sky.commons.domain.Work;
import com.sky.worker.domain.WorkRepository;
import com.sky.worker.domain.WorkerRepository;
import com.sky.worker.exception.WorkingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class Worker {
  private static final Logger logger = LoggerFactory.getLogger(Worker.class);

  @Autowired
  private Processor processor;

  @Autowired
  private WorkRepository workRepository;

  @Autowired
  private WorkerRepository workerRepository;

  private long id;

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  private void changeStatus(com.sky.commons.domain.Worker.State state) {
    com.sky.commons.domain.Worker worker = workerRepository.findOne(id);
    worker.setState(state);
    workerRepository.save(worker);
  }

  private void start() {
    changeStatus(com.sky.commons.domain.Worker.State.WORKING);
  }

  private void finish(Work work) {
    changeStatus(com.sky.commons.domain.Worker.State.IDLE);

    MethodLog[] methodLogs = workRepository.findMethodLogs(work.getId());

    long sum = 0, mostLongTime = Long.MIN_VALUE;
    for (MethodLog methodLog : methodLogs) {
      long t = methodLog.getElapsedTime();
      sum += t;
      if (mostLongTime < t)
        mostLongTime = t;
    }
    work.setAverageTime((double)sum/(double)methodLogs.length);
    work.setMostLongTime(mostLongTime);
    work.setFinished(true);

    workRepository.update(work);
  }

  public void doWork(long workId) throws WorkingException {
    Work work = workRepository.findOne(workId);
    ExecutionUnit eu = workRepository.findExecutionUnitById(workId);

    File file = null;
    try {

      file = setup(eu.getJarFile(), eu.getJarFileName());
      start();

      Process process = processor.process(work.getId(), file.getAbsolutePath(), eu.getArguments());
      IOUtils.copy(process.getInputStream(), System.out);
      IOUtils.copy(process.getErrorStream(), System.err);
      int exitCode = process.waitFor();
      logger.debug("exitCode: {}", exitCode);

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
      finish(work);
    }
  }

  private File setup(byte []jarFile, String name) throws IOException {
    File file = new File(FileUtils.getTempDirectoryPath() + "/" + name);
    FileUtils.writeByteArrayToFile(file, jarFile);

    return file;
  }
}
