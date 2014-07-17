package com.sky.profiler.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.commons.RealtimeMethodProfile;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class SkyAPITest {
  private static final int PORT = 8080;
  MockWebServer server;

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
  }

  @After
  public void tearDown() throws Exception {
    server.shutdown();
  }

  @Test
  public void testCreate() throws Exception {
    server.enqueue(new MockResponse().setBody(Long.toString(11L)));

    server.play(PORT);

    long id = SkyAPI.create();

    assertEquals(11L, id);

    RecordedRequest request = server.takeRequest();
    assertEquals("/agent/profile", request.getPath());
    assertEquals("POST", request.getMethod());
  }

  @Test
  public void testPut() throws Exception {
    RealtimeMethodProfile profile = new RealtimeMethodProfile();
    profile.setCaller(null);
    profile.setMethod(null);
    profile.setElapsedTime(11L);
    profile.setThrowable(null);

    server.enqueue(new MockResponse().setBody(Long.toString(11L)));
    server.enqueue(new MockResponse());

    server.play(PORT);

    long id = SkyAPI.create();

    profile.setProfileId(id);

    SkyAPI.put(profile);

    assertEquals(11L, id);

    RecordedRequest request = server.takeRequest();
    assertEquals("/agent/profile", request.getPath());
    assertEquals("POST", request.getMethod());

    request = server.takeRequest();
    assertEquals(toJsonString(profile), request.getUtf8Body());
    assertEquals("/agent/profile", request.getPath());
    assertEquals("PUT", request.getMethod());
  }

  private String toJsonString(RealtimeMethodProfile profile) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(profile);
  }

  private <T> byte []toBytes(T value) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(baos);
      oos.writeObject(value);

      return baos.toByteArray();
    } finally {
      if (oos != null) oos.close();
    }
  }
}