package com.sky.server.mvc.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jcooky on 2014. 8. 3..
 */
@Entity
public class Work {
  @Id
  @GeneratedValue
  private long id;

  @Column(updatable = false, unique = true)
  private Long ordering;

  private long startTime = System.currentTimeMillis();
  private double averageTime;
  private long mostLongTime;

  @ManyToOne
  private ExecutionUnit executionUnit;

  @ManyToOne
  private Worker worker;

  @OneToMany
  @OrderBy("ordering ASC")
  private Set<MethodLog> methodLogs = new HashSet<MethodLog>();

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

  public Long getOrdering() {
    return ordering;
  }

  public void setOrdering(Long ordering) {
    this.ordering = ordering;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Work work = (Work) o;

    if (id != work.id) return false;
    if (ordering != null ? !ordering.equals(work.ordering) : work.ordering != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (ordering != null ? ordering.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Work{" +
        "mostLongTime=" + mostLongTime +
        ", averageTime=" + averageTime +
        ", ordering=" + ordering +
        ", id=" + id +
        '}';
  }

  public Set<MethodLog> getMethodLogs() {
    return methodLogs;
  }

  public void setMethodLogs(Set<MethodLog> methodLogs) {
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
}
