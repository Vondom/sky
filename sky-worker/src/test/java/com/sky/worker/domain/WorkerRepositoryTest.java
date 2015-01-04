package com.sky.worker.domain;

import com.sky.commons.domain.Worker;
import com.sky.worker.config.SkyWorkerConfigProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class WorkerRepositoryTest {

  @InjectMocks
  private WorkerRepository workerRepository;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private SkyWorkerConfigProperties properties;

  String path = "http://localhost:12322";

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    doReturn(path).when(properties).getServerConnection();
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testSave() throws Exception {
    Worker worker = new Worker();
    worker.setId(11L);
    worker.setState(Worker.State.IDLE);

    doReturn(worker).when(restTemplate).postForObject(eq(path + "/api/worker"), eq(worker), eq(Worker.class));

    Worker returnedWorker = workerRepository.save(worker);

    assertThat(worker, is(notNullValue()));
    assertThat(returnedWorker, is(worker));

    verify(restTemplate).postForObject(eq(path + "/api/worker"), eq(worker), eq(Worker.class));
  }

  @Test
  public void testFindOne() throws Exception {
    Worker worker = new Worker();
    worker.setId(11L);
    worker.setState(Worker.State.IDLE);

    doReturn(worker).when(restTemplate).getForObject(eq(path + "/api/worker/{id}"), eq(Worker.class), eq(11L));

    Worker returnedWorker = workerRepository.findOne(11L);

    assertThat(worker, is(notNullValue()));
    assertThat(returnedWorker, is(worker));

    verify(restTemplate).getForObject(eq(path + "/api/worker/{id}"), eq(Worker.class), eq(11L));
  }

  @Test
  public void testDelete() throws Exception {
    workerRepository.delete(11L);


    verify(restTemplate).delete(eq(path + "/api/worker/{id}"), eq(11L));
  }
}