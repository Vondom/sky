package com.sky.worker;

import com.sky.commons.Jar;
import com.sky.commons.Work;
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
  private Processor processor;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testDoWork() throws Exception {
    try {
      String error = "error test", stdout = "input test";

      Work work = new Work()
          .setId(10L)
          .setProjectId(11L)
          .setArguments("test_arguments")
          .setJar(new Jar()
              .setName("test.jar")
              .setFile("test".getBytes()));
      Process process = mock(Process.class);

      doReturn(process).when(processor).process(eq(11L), eq(10L), anyString());
      doReturn(0).when(process).waitFor();
      doReturn(new ByteArrayInputStream(error.getBytes())).when(process).getErrorStream();
      doReturn(new ByteArrayInputStream(stdout.getBytes())).when(process).getInputStream();

      worker.doWork(work);

      verify(processor).process(eq(work.projectId), eq(work.id), anyString());
      verify(process).getErrorStream();
      verify(process).getInputStream();
    } finally {
      File file = new File(FileUtils.getTempDirectoryPath() + "test.jar");
      if (file.exists())
        file.delete();
    }
  }
}