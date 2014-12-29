package com.sky.worker.service;

import com.sky.commons.model.ExecutionUnit;
import com.sky.commons.model.Work;
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

  private void changeStatus(com.sky.commons.model.Worker.State state) {
    com.sky.commons.model.Worker worker = workerRepository.findOne(id);
    worker.setState(state);
    workerRepository.save(worker);
  }

  public void doWork(long workId) throws WorkingException {
    Work work = workRepository.findOne(workId);
    ExecutionUnit eu = workRepository.findExecutionUnitById(workId);

    File file = null;
    try {

      file = setup(eu.getJarFile(), eu.getJarFileName());
      changeStatus(com.sky.commons.model.Worker.State.WORKING);

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
      changeStatus(com.sky.commons.model.Worker.State.IDLE);
    }
  }

  private File setup(byte []jarFile, String name) throws IOException {
    File file = new File(FileUtils.getTempDirectoryPath() + "/" + name);
    FileUtils.writeByteArrayToFile(file, jarFile);

    return file;
  }
}
