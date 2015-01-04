package com.sky.worker.domain;

import com.sky.commons.domain.ExecutionUnit;
import com.sky.commons.domain.Work;
import org.springframework.stereotype.Repository;

/**
 * Created by JCooky on 14. 12. 29..
 */
@Repository
public class WorkRepository extends AbstractRestResourceRepository {

  @Override
  protected String requestPath() {
    return "/api/work";
  }

  public Work save(Work work) {
    return restTemplate.postForObject(path(""), work, Work.class);
  }

  public Work findOne(long id) {
    return restTemplate.getForObject(path("/{id}"), Work.class, id);
  }

  public ExecutionUnit findExecutionUnitById(long id) {
    return restTemplate.getForObject(path("/{id}/execution-unit"), ExecutionUnit.class, id);
  }
}
