package com.sky.server.mvc.repository;

import com.sky.server.mvc.model.Project;
import com.sky.server.mvc.model.User;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class ProjectRepositoryTest extends SpringBasedTestSupport {

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private UserRepository userRepository;

  @Before
  public void setUp() {
    User user = new User();
    user.setId(11L);
    user.setEmail("a@a.com");
    userRepository.save(user);
  }

  @After
  public void tearDown() {
    projectRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void testSave() {

    User user = userRepository.findOne(11L);

    assertNotNull(user);

    Project project = new Project();
    project.setId(2L);
    project.setOwner(user);
    project.setJarFile(null);
    project.setJarFileName("test");
    projectRepository.save(project);

    project = projectRepository.findOne(2L);

    assertNotNull(project);
  }
}