package com.sky.worker.domain;

import com.sky.commons.domain.Worker;
import org.springframework.stereotype.Repository;

/**
 * Created by JCooky on 14. 12. 29..
 */
@Repository
public class WorkerRepository extends AbstractRestResourceRepository {

  @Override
  protected String requestPath() {
    return "/api/worker";
  }

  public Worker save(Worker worker) {
    return restTemplate.postForObject(path(""), worker, Worker.class);
  }

  public Worker findOne(long id) {
    return restTemplate.getForObject(path("/{id}"), Worker.class, id);
  }

  public void delete(long id) {
    restTemplate.delete(path("/{id}"), id);
  }
}
