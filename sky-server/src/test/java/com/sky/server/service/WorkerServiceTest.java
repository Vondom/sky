package com.sky.server.service;

import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerServiceTest {
  private static final Logger logger = LoggerFactory.getLogger(WorkerServiceTest.class);

  private static final int PORT = 53121;


  @InjectMocks
  private WorkerService workerService;

}
