package com.sky.worker;

import com.sky.commons.Jar;
import com.sky.commons.Work;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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
  private Options options;

  private byte []contents = new byte[] {12,11,10,9,8,7,6,5,4,3,2,1};
  private MockWebServer server;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    server = new MockWebServer();
    server.enqueue(new MockResponse().setBody(contents));
    server.play(18080);
  }

  @After
  public void tearDown() throws Exception {
    server.shutdown();

    File file = new File(FileUtils.getTempDirectoryPath() + "test.jar");
    if (file.exists())
      assertTrue(file.delete());

    file = new File(Worker.PROFILER_PATH);
    if (file.exists())
      assertTrue(file.delete());
  }

  @Test
  public void testDoWork() throws Exception {
    String error = "error test", stdout = "input test";

    Work work = new Work()
        .setId(10L)
        .setArguments("test_arguments")
        .setJar(new Jar()
            .setName("test.jar")
            .setFile("test".getBytes()));
    Process process = mock(Process.class);

    doReturn("localhost:18080").when(options).get(Options.Key.HOST);
    doReturn(process).when(processor).process(eq(10L), anyString());
    doReturn(0).when(process).waitFor();
    doReturn(new ByteArrayInputStream(error.getBytes())).when(process).getErrorStream();
    doReturn(new ByteArrayInputStream(stdout.getBytes())).when(process).getInputStream();

    worker.doWork(work);

    verify(processor).process(eq(work.id), anyString());
    verify(process).getErrorStream();
    verify(process).getInputStream();

    RecordedRequest request = server.takeRequest();
    assertThat(request.getPath(), is("/sky-profiler.jar"));
    assertThat(request.getMethod(), is("GET"));
  }
}