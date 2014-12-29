package com.sky.server.mvc.repository;

import com.sky.commons.model.Project;
import com.sky.commons.model.User;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class ProjectRepositoryTest extends SpringBasedTestSupport {

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private UserRepository userRepository;

  @After
  public void tearDown() {
    projectRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void testSave() {

    User user = new User();
    user.setId(11L);
    user.setEmail("a@a.com");
    user = userRepository.save(user);

    assertNotNull(user);

    Project project = new Project();
    project.setId(2L);
    project.setOwner(user);
    project = projectRepository.save(project);

    assertNotNull(project);
  }
}