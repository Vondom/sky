package com.sky.server.service;

import com.sky.server.mvc.model.Work;
import org.apache.thrift.TException;

import java.util.concurrent.Future;

/**
 * Created by jcooky on 2014. 9. 12..
 */
public interface WorkerService {
  public Future<Boolean> doWork(Work work) throws TException;
  public void checkWorkers() throws TException;
}
