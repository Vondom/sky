package com.sky.server.domain.custom;

import com.sky.commons.domain.ExecutionUnit;

import java.util.List;

/**
 * Created by JCooky on 15. 1. 6..
 */
public interface ExecutionUnitRepositoryCustom {
  List<ExecutionUnit> findByAccountAndProjectNameAndBranchName(String account, String projectName, String branchName);
}
