package com.sky.worker;

import com.sky.commons.Jar;
import com.sky.commons.Work;
import com.sky.commons.WorkerControlService;
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
  private WorkerControlService.Iface workerControlService;

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

    Work work = new Work()
        .setId(10L)
        .setArguments("test_arguments")
        .setJar(new Jar()
            .setName("test.jar")
            .setFile("test".getBytes()));
    Process process = mock(Process.class);

    doReturn(process).when(processor).process(eq(work.id), anyString(), eq(work.arguments));
    doReturn(0).when(process).waitFor();
    doReturn(new ByteArrayInputStream(error.getBytes())).when(process).getErrorStream();
    doReturn(new ByteArrayInputStream(stdout.getBytes())).when(process).getInputStream();

    worker.doWork(work);

    verify(processor).process(eq(work.id), anyString(), eq(work.arguments));
    verify(process).getErrorStream();
    verify(process).getInputStream();
    verify(workerControlService).done(eq(worker.getId()), eq(work.id));
  }
}