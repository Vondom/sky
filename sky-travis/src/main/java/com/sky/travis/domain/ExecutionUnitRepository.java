package com.sky.travis.domain;

import com.sky.commons.domain.ExecutionUnit;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JCooky on 15. 1. 24..
 */
@Repository
public class ExecutionUnitRepository extends AbstractRestResourceRepository {
  private RestTemplate restTEmplate = new RestTemplate();

  @Override
  protected String requestPath() {
    return "/api/execution-unit";
  }

  public ExecutionUnit get(String account, String projectName, String branchName) {
    return restTEmplate.getForObject(path("/account/{account}/project/{projectName}/branch/{branchName}"), ExecutionUnit.class, account, projectName, branchName);
  }
}
