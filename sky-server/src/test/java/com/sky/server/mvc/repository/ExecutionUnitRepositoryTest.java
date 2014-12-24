package com.sky.server.mvc.repository;

import com.sky.server.mvc.model.ExecutionUnit;
import com.sky.server.mvc.model.Project;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.base.CharMatcher.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExecutionUnitRepositoryTest extends SpringBasedTestSupport {

  @Autowired
  private ExecutionUnitRepository executionUnitRepository;

  @Autowired
  private ProjectRepository projectRepository;

  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {
    projectRepository.deleteAll();
    executionUnitRepository.deleteAll();
  }

  @Test
  public void testSave() {
    Project project = new Project();
    project.setName("test");
    project = projectRepository.save(project);
    assertThat(project, is(notNullValue()));

    ExecutionUnit executionUnit = new ExecutionUnit();
    executionUnit.setJarFile(new byte[]{1, 1, 1, 1});
    executionUnit.setJarFileName("test.jar");
    executionUnit.setArguments("test1234");
    executionUnit.setProject(new Project());
    executionUnit.getProject().setId(project.getId());
    executionUnit = executionUnitRepository.save(executionUnit);

    executionUnit = executionUnitRepository.findOne(executionUnit.getId());

    assertThat(executionUnit.getProject().getName(), is(project.getName()));

    assertThat(executionUnit, is(notNullValue()));
  }
}