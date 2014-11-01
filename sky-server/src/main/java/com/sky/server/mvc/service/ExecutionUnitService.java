package com.sky.server.mvc.service;

import com.sky.server.mvc.model.ExecutionUnit;
import com.sky.server.mvc.repository.ExecutionUnitRepository;
import com.sky.server.mvc.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by jcooky on 14. 10. 28..
 */
@Service
public class ExecutionUnitService {
  private static final Logger logger = LoggerFactory.getLogger(ExecutionUnitService.class);

  @Autowired
  private ExecutionUnitRepository executionUnitRepository;

  @Autowired
  private ProjectRepository projectRepository;

  @Transactional
  public ExecutionUnit save(ExecutionUnit executionUnit) {
    return executionUnitRepository.save(executionUnit);
  }

  @Transactional
  public ExecutionUnit create(ExecutionUnit executionUnit) {
    logger.trace("{}", executionUnit.getProject());

    executionUnit.setProject(projectRepository.findOne(executionUnit.getProject().getId()));
    return save(executionUnit);
  }
}
