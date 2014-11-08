package com.sky.server.mvc.model;

import javax.persistence.*;

/**
 * Created by jcooky on 2014. 7. 4..
 */
@Entity
public class MethodLog {
  @Id
  @GeneratedValue
  private Long id = null;

  private long elapsedTime;
  private long startTime;
  private long ordering;

  private String threadName;

  @ManyToOne
  private MethodKey methodKey;

  @ManyToOne
  private MethodKey caller;
  private long totalElapsedTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

  public void setElapsedTime(long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public MethodKey getMethodKey() {
    return methodKey;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getOrdering() {
    return ordering;
  }

  public void setOrdering(long ordering) {
    this.ordering = ordering;
  }

  public MethodKey getCaller() {
    return caller;
  }

  public void setCaller(MethodKey caller) {
    this.caller = caller;
  }

  public void setMethodKey(MethodKey methodKey) {
    this.methodKey = methodKey;
  }

  public String getThreadName() {
    return threadName;
  }

  public void setThreadName(String threadName) {
    this.threadName = threadName;
  }

  public void setTotalElapsedTime(long totalElapsedTime) {
    this.totalElapsedTime = totalElapsedTime;
  }

  public long getTotalElapsedTime() {
    return totalElapsedTime;
  }
}
