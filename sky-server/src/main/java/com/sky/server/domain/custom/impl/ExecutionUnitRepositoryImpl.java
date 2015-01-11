package com.sky.server.domain.custom.impl;

import com.mysema.query.jpa.impl.JPAQuery;
import com.sky.commons.domain.ExecutionUnit;
import com.sky.commons.domain.QExecutionUnit;
import com.sky.commons.domain.QProject;
import com.sky.commons.domain.QUser;
import com.sky.server.domain.custom.ExecutionUnitRepositoryCustom;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.List;

public class ExecutionUnitRepositoryImpl implements ExecutionUnitRepositoryCustom {
  @Resource
  private EntityManager em;

  @Override
  public List<ExecutionUnit> findByAccountAndProjectNameAndBranchName(String account, String projectName, String branchName) {
    QProject project = new QProject("project");
    QExecutionUnit executionUnit = new QExecutionUnit("executionUnit");
    QUser user = new QUser("user");

    return new JPAQuery(em)
        .from(project, executionUnit, user)
        .where(executionUnit.project.eq(project), project.name.eq(projectName), project.owner.eq(user), user.name.eq(account), executionUnit.name.eq(branchName))
        .listResults(executionUnit)
        .getResults();
  }
}
