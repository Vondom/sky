package com.sky.server.mvc.repository.custom;

import com.sky.commons.model.Work;

/**
 * Created by jcooky on 2014. 8. 4..
 */
public interface WorkRepositoryCustom {
  Work findReadyWork();
  Work findLastWork();
}
