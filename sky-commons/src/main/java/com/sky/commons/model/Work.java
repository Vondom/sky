package com.sky.commons.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcooky on 2014. 8. 3..
 */
@Entity
public class Work {
  @Id
  @GeneratedValue
  private long id;

  private boolean finished = false;
  private long createTime = System.currentTimeMillis();
  private long startTime = System.currentTimeMillis();
  private double averageTime;
  private long mostLongTime;

  @ManyToOne
  private ExecutionUnit executionUnit;

  @ManyToOne
  private Worker worker;

  @OneToMany
  @OrderBy("ordering ASC")
  private List<MethodLog> methodLogs = new ArrayList<MethodLog>();

  public Worker getWorker() {
    return worker;
  }

  public void setWorker(Worker worker) {
    this.worker = worker;
  }

  public ExecutionUnit getExecutionUnit() {
    return executionUnit;
  }

  public void setExecutionUnit(ExecutionUnit project) {
    this.executionUnit = project;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Work work = (Work) o;

    if (id != work.id) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "Work{" +
        "mostLongTime=" + mostLongTime +
        ", averageTime=" + averageTime +
        ", id=" + id +
        '}';
  }

  public List<MethodLog> getMethodLogs() {
    return methodLogs;
  }

  public void setMethodLogs(List<MethodLog> methodLogs) {
    this.methodLogs = methodLogs;
  }

  public double getAverageTime() {
    return averageTime;
  }

  public void setAverageTime(double averageTime) {
    this.averageTime = averageTime;
  }

  public long getMostLongTime() {
    return mostLongTime;
  }

  public void setMostLongTime(long mostLongTime) {
    this.mostLongTime = mostLongTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }
}
