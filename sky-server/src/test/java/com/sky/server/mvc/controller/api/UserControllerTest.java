package com.sky.server.mvc.controller.api;

import com.sky.commons.domain.Project;
import com.sky.commons.domain.User;
import com.sky.server.domain.UserRepository;
import com.sky.server.test.SpringBasedTestSupport;
import com.sky.server.web.rest.UserController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends SpringBasedTestSupport {

  @InjectMocks
  private UserController userController;

  @Mock
  private UserRepository userRepository;



  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);


  }

  @Test
  public void testGetProjects() throws Exception {
    List<Project> projects = new ArrayList<Project>();
    Project prj = new Project();
    prj.setId(2);
    projects.add(prj);
    prj = new Project();
    prj.setId(3);
    projects.add(prj);
    prj = new Project();
    prj.setId(4);
    projects.add(prj);

    User user = new User();
    user.setId(1L);
    user.setEmail("bak723@gmail.com");
    user.setProjects(projects);

    when(userRepository.findOne(eq(1L))).thenReturn(user);

    MockMvcBuilders.standaloneSetup(userController)
        .build()
        .perform(MockMvcRequestBuilders.get("/api/user/{id}/projects", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].id", is(2)))
        .andExpect(jsonPath("$[1].id", is(3)))
        .andExpect(jsonPath("$[2].id", is(4)))
        .andDo(print());

    verify(userRepository).findOne(eq(1L));
  }
}
