package com.sky.server.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.commons.RealtimeMethodProfile;
import com.sky.server.mvc.model.Profile;
import com.sky.server.mvc.service.ProfileProcessService;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileProcessControllerTest extends SpringBasedTestSupport {
  @Autowired
  private ObjectMapper objectMapper;

  @InjectMocks
  private ProfileProcessController controller;

  @Mock
  private ProfileProcessService profileProcessService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCreate() throws Exception {
    Profile profile = new Profile();

    long id = Long.parseLong(MockMvcBuilders.standaloneSetup(controller)
        .build()
        .perform(post("/agent/profile").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString());

    verify(profileProcessService).create(profile);
    assertEquals(0L, id);
  }

  @Test
  public void testPut() throws Exception {
    RealtimeMethodProfile profile = new RealtimeMethodProfile();
    profile.setCaller(null);
    profile.setMethod(null);
    profile.setElapsedTime(11L);
    profile.setThrowable(null);
    profile.setProfileId(11L);

    MockMvcBuilders.standaloneSetup(controller)
        .build()
        .perform(put("/agent/profile").content(toBytes(profile)))
        .andExpect(status().isOk());

    verify(profileProcessService).put(profile);

  }

  private byte[] toBytes(RealtimeMethodProfile profile) throws IOException {
    ByteArrayOutputStream os = null;
    try {
      os = new ByteArrayOutputStream();
      new ObjectOutputStream(os).writeObject(profile);

      return os.toByteArray();
    } finally {
      if (os != null) os.close();
    }
  }

  private String toJsonString(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }
}