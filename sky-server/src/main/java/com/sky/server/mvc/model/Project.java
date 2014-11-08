package com.sky.server.mvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcooky on 2014. 7. 21..
 */
@Entity
public class Project {
  @Id
  @GeneratedValue
  private long id;

  private String name;
  private String description;
  private long createTime = System.currentTimeMillis();

  @OneToMany(mappedBy = "project")
  @JsonIgnore
  private List<ExecutionUnit> executionUnits = new ArrayList<ExecutionUnit>();

  @ManyToOne
  private User owner;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }


  @Override
  public String toString() {
    return "Project{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", createTime=" + createTime +
        ", owner=" + owner +
        '}';
  }


  public List<ExecutionUnit> getExecutionUnits() {
    return executionUnits;
  }

  public void setExecutionUnits(List<ExecutionUnit> executionUnits) {
    this.executionUnits = executionUnits;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
