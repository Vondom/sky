package com.sky.worker.service;

import com.sky.commons.Jar;
import com.sky.commons.Work;
import com.sky.commons.WorkerControlService;
import com.sky.worker.Options;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.ByteArrayInputStream;
import java.io.File;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class WorkerTest {
  @InjectMocks
  @Spy
  private Worker worker;

  @Mock
  private Runner runner;

  @Mock
  private WorkerControlService.Iface workManager;

  @Mock
  private Options options;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testDoWork() throws Exception {
    try {
      String error = "error test", stdout = "input test";

      Work work = new Work()
          .setProjectId(11L)
          .setArguments("test_arguments")
          .setJar(new Jar()
              .setName("test.jar")
              .setFile("test".getBytes()));
      Process process = mock(Process.class);

      doReturn(Integer.toString(9200)).when(options).get(Options.Key.PORT);
      doReturn(process).when(runner).run(eq(11L), anyString());
      doReturn(0).when(process).waitFor();
      doReturn(new ByteArrayInputStream(error.getBytes())).when(process).getErrorStream();
      doReturn(new ByteArrayInputStream(stdout.getBytes())).when(process).getInputStream();

      worker.doWork(work);

      verify(runner).run(eq(work.projectId), anyString());
      verify(process).getErrorStream();
      verify(process).getInputStream();
    } finally {
      File file = new File(FileUtils.getTempDirectoryPath() + "test.jar");
      if (file.exists())
        file.delete();
    }
  }
}