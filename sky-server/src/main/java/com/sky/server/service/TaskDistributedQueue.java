package com.sky.server.service;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by JCooky on 14. 12. 27..
 */
@Service
public class TaskDistributedQueue {

  @Autowired
  private SimpleDistributedQueue simpleDistributedQueue;

  public boolean put(long workId) throws Exception {
    return simpleDistributedQueue.offer(SerializationUtils.serialize(workId));
  }

  public long take() throws Exception {
    return SerializationUtils.deserialize(simpleDistributedQueue.take());
  }

}
