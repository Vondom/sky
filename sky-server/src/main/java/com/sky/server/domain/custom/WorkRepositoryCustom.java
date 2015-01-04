package com.sky.server.domain.custom;

import com.sky.commons.domain.Work;

/**
 * Created by jcooky on 2014. 8. 4..
 */
public interface WorkRepositoryCustom {
  Work findReadyWork();
  Work findLastWork();
}
