package com.sky.worker.domain;

import com.sky.commons.model.ExecutionUnit;
import com.sky.commons.model.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

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
