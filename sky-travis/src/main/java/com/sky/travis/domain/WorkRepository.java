package com.sky.travis.domain;

import com.sky.commons.domain.ExecutionUnit;
import com.sky.commons.domain.MethodLog;
import com.sky.commons.domain.Work;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 * Created by JCooky on 14. 12. 29..
 */
@Repository
public class WorkRepository extends AbstractRestResourceRepository {

  private RestTemplate restTemplate = new RestTemplate();

  @Override
  protected String requestPath() {
    return "/api/work";
  }

  public Work create(Work work) {
    return restTemplate.postForObject(path(""), work, Work.class);
  }
}
