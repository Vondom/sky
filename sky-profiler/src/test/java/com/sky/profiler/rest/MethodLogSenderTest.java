package com.sky.profiler.rest;

import com.sky.commons.domain.MethodLog;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class MethodLogSenderTest {
  @InjectMocks
  private MethodLogSender sender;

  @Mock
  private RestTemplate restTemplate;

  private String path = "http://localhost:8080/api/method-log";

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testPut() throws Exception {
    MethodLog methodLog = new MethodLog();
    methodLog.setId(11L);

    sender.start();
    sender.send(path, methodLog);
    Thread.sleep(1000);
    sender.finish();

    verify(restTemplate).postForObject(eq(path), eq(methodLog), eq(MethodLog.class));
  }
}