package com.sky.worker;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class OptionsTest {
  private Properties env;
  private Options options = new Options();

  @Before
  public void setUp() throws IOException {
    env = new Properties();
    env.load(ClassLoader.getSystemResourceAsStream("application.properties"));
  }

  @Test
  public void testGetDefaults() throws ParseException, IOException {
    options.init(new String[]{});

    assertEquals(env.getProperty("worker.options.name"), options.get(Options.Key.NAME));
  }
}