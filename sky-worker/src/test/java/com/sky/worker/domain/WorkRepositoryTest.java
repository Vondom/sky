package com.sky.worker.domain;

import com.sky.commons.domain.ExecutionUnit;
import com.sky.commons.domain.Work;
import com.sky.worker.config.SkyWorkerConfigProperties;
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

public class WorkRepositoryTest {

  @InjectMocks
  private WorkRepository workRepository;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private SkyWorkerConfigProperties properties;

  private String path = "http://localhost:8080";

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    doReturn(path).when(properties).getServerConnection();
  }

  @Test
  public void testUpdate() throws Exception {
    Work work = new Work();
    work.setId(11L);

    doReturn(work).when(restTemplate).put(eq(path + "/api/work/{id}"), eq(work), work.getId());

    workRepository.update(work);

    assertThat(work, is(notNullValue()));

    verify(restTemplate).put(eq(path + "/api/work/{id}"), eq(work), work.getId());
  }


  @Test
  public void testFindOne() throws Exception {
    Work work = new Work();
    work.setId(11L);

    doReturn(work).when(restTemplate).getForObject(eq(path + "/api/work/{id}"), eq(Work.class), eq(11L));

    Work returnedWork = workRepository.findOne(11L);

    assertThat(work, is(notNullValue()));
    assertThat(returnedWork, is(work));

    verify(restTemplate).getForObject(eq(path + "/api/work/{id}"), eq(Work.class), eq(11L));
  }

  @Test
  public void testFindExecutionUnitById() throws Exception {
    ExecutionUnit executionUnit = new ExecutionUnit();
    executionUnit.setId(11L);

    doReturn(executionUnit).when(restTemplate).getForObject(eq(path + "/api/work/{id}/execution-unit"), eq(ExecutionUnit.class), eq(11L));

    ExecutionUnit returnedWork = workRepository.findExecutionUnitById(11L);

    assertThat(executionUnit, is(notNullValue()));
    assertThat(returnedWork, is(executionUnit));

    verify(restTemplate).getForObject(eq(path + "/api/work/{id}/execution-unit"), eq(ExecutionUnit.class), eq(11L));
  }
}