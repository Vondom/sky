package com.sky.profiler.jrat.provider;

/**
 * Created by jcooky on 2014. 7. 30..
 */
public interface RealTimeMethodHandlerFactoryMBean {
  void setProjectId(Long projectId);
  Long getProjectId();
  void setWorkId(Long workId);
  Long getWorkId();
}
