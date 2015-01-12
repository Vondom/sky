package com.sky.worker.domain;

import com.sky.commons.domain.ExecutionUnit;
import com.sky.commons.domain.MethodLog;
import com.sky.commons.domain.Work;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by JCooky on 14. 12. 29..
 */
@Repository
public class WorkRepository extends AbstractRestResourceRepository {

  @Override
  protected String requestPath() {
    return "/api/work";
  }

  public void update(Work work) {
    restTemplate.put(path("/{id}"), work, work.getId());
  }

  public Work findOne(long id) {
    return restTemplate.getForObject(path("/{id}"), Work.class, id);
  }

  public ExecutionUnit findExecutionUnitById(long id) {
    return restTemplate.getForObject(path("/{id}/execution-unit"), ExecutionUnit.class, id);
  }

  public MethodLog[] findMethodLogs(long id) {
    return restTemplate.getForObject(path("/{id}/method-logs"), MethodLog[].class, id);
  }
}
