package com.sky.server.mvc.service;

import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.WorkerRepository;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkerServiceTest extends SpringBasedTestSupport {

  @InjectMocks
  private WorkerService workerService;

  @Mock
  public WorkerRepository workerRepository;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testDoWork() throws Exception {
    Work work = new Work();
    List<Worker> workers = mock(List.class);

    when(workers.isEmpty()).thenReturn(false);
    when(workerRepository.findByState(eq(Worker.State.IDLE))).thenReturn(workers);

    assertTrue(workerService.doWork(work).get());

    verify(workerService).doWork(eq(work));
  }
}
