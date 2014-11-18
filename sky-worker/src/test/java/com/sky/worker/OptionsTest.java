package com.sky.worker;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.env.MockEnvironment;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class OptionsTest {
  @InjectMocks
  private Options options = new Options();

  @Mock
  private MockEnvironment env = new MockEnvironment();

  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);

    Properties props = new Properties();
    props.load(ClassLoader.getSystemResourceAsStream("application.properties"));

    for (Object key_ : props.keySet()) {
      String key = key_.toString();
      String value = props.getProperty(key);

      env.withProperty(key, value);
    }
  }

  @Test
  public void testGetDefaults() throws ParseException, IOException {
    options.init(new String[]{});

    assertEquals(env.getProperty("worker.options.name"), options.get(Options.Key.NAME));
  }
}