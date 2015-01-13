package com.sky.worker;

import com.sky.commons.domain.ExecutionUnit;
import com.sky.commons.domain.MethodLog;
import com.sky.commons.domain.Work;
import com.sky.worker.domain.WorkRepository;
import com.sky.worker.domain.WorkerRepository;
import com.sky.worker.service.Processor;
import com.sky.worker.service.Worker;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.ByteArrayInputStream;
import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class WorkerTest {
  @InjectMocks
  @Spy
  private Worker worker;

  @Mock
  private Processor processor;

  @Mock
  private WorkRepository workRepository;

  @Mock
  private WorkerRepository workerRepository;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    worker.setId(11L);
  }

  @After
  public void tearDown() throws Exception {

    File file = new File(FileUtils.getTempDirectoryPath() + "test.jar");
    if (file.exists())
      assertTrue(file.delete());

  }

  @Test
  public void testDoWork() throws Exception {
    String error = "error test\n", stdout = "input test\n";

    ExecutionUnit eu = new ExecutionUnit();
    eu.setArguments("test_arguments");
    eu.setJarFile("test".getBytes());
    eu.setJarFileName("test.jar");

    Work work = new Work();
    work.setId(10L);
    work.setExecutionUnit(eu);

    com.sky.commons.domain.Worker worker = new com.sky.commons.domain.Worker();
    worker.setId(11L);

    Process process = mock(Process.class);

    doReturn(work).when(workRepository).findOne(eq(10L));
    doReturn(eu).when(workRepository).findExecutionUnitById(eq(10L));
    doReturn(worker).when(workerRepository).findOne(anyLong());
    doReturn(new MethodLog[]{}).when(workRepository).findMethodLogs(eq(10L));
    doReturn(process).when(processor).process(eq(work.getId()), anyString(), eq(eu.getArguments()));
    doReturn(0).when(process).waitFor();
    doReturn(new ByteArrayInputStream(error.getBytes())).when(process).getErrorStream();
    doReturn(new ByteArrayInputStream(stdout.getBytes())).when(process).getInputStream();


    this.worker.doWork(10L);

    verify(workerRepository, times(2)).save(any(com.sky.commons.domain.Worker.class));
    verify(workRepository).findOne(eq(10L));
    verify(processor).process(eq(work.getId()), anyString(), eq(eu.getArguments()));
    verify(process).getErrorStream();
    verify(process).getInputStream();
  }
}