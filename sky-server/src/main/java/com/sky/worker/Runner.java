package com.sky.worker;

import com.sky.commons.Work;
import com.sky.commons.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jcooky on 2014. 7. 30..
 */
@Service
public class Runner implements Runnable {
  private static final Logger logger = LoggerFactory.getLogger(Runner.class);

  private BlockingQueue<Work> works = new LinkedBlockingQueue<Work>();

  private Thread workingThread = null;

  @Autowired
  private Worker.Iface worker;

  @PostConstruct
  public void init() throws Exception {
    workingThread = new Thread(this);
  }

  public void run() {
    Work work = null;

    try {
      while ((work = works.take()) != null) {
        try {
          worker.doWork(work);
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }
      }
    } catch (InterruptedException e) {
      logger.info("Interrupted");
    }
  }

  @PreDestroy
  public void destroy() {
    if (!workingThread.isInterrupted()) {
      workingThread.interrupt();
    }
  }
}
