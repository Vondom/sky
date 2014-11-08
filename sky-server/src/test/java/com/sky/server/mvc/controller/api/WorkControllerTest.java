package com.sky.server.mvc.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.server.mvc.model.Work;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WorkControllerTest extends SpringBasedTestSupport {

  @Autowired
  private WorkController workController;

  @Autowired
  private ObjectMapper objectMapper;

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void testCreate() throws Exception {
    Work work = new Work();

    MockMvcBuilders.standaloneSetup(workController)
        .build().perform(MockMvcRequestBuilders.post("/api/work").content(objectMapper.writeValueAsString(work)).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
