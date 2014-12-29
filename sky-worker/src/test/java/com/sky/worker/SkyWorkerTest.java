package com.sky.worker;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SkyWorkerTest {

  @InjectMocks
  private SkyWorker skyWorker;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSetUpProfiler() throws Exception {
    try {
      skyWorker.setUpProfiler();

      assertThat(new File(SkyWorker.PROFILER_PATH).exists(), is(true));
    } finally {
      FileUtils.forceDelete(new File(SkyWorker.PROFILER_PATH));
    }
  }
}