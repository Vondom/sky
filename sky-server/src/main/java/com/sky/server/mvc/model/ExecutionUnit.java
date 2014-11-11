package com.sky.server.mvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcooky on 2014. 10. 17..
 */
@Entity
public class ExecutionUnit {
  @Id
  @GeneratedValue
  private long id;

  private String name;

  @Column(length = Integer.MAX_VALUE)
  private byte[] jarFile;
  private String jarFileName;

  private String arguments;

  //@NotNull(message = "Main class name must be not null")
  private String mainClassName;

  @ManyToOne
  private Project project;

  @OneToMany(mappedBy = "executionUnit", cascade = CascadeType.ALL)
  @OrderBy("ordering ASC")
  @JsonIgnore
  private List<Work> works = new ArrayList<Work>();

  public byte[] getJarFile() {
    return jarFile;
  }

  public void setJarFile(byte[] jarFile) {
    this.jarFile = jarFile;
  }

  public String getArguments() {
    return arguments;
  }

  public void setArguments(String arguments) {
    this.arguments = arguments;
  }

  public String getJarFileName() {
    return jarFileName;
  }

  public void setJarFileName(String jarFileName) {
    this.jarFileName = jarFileName;
  }

  public List<Work> getWorks() {
    return works;
  }

  public void setWorks(List<Work> works) {
    this.works = works;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMainClassName() {
    return mainClassName;
  }

  public void setMainClassName(String mainClassName) {
    this.mainClassName = mainClassName;
  }
}
