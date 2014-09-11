package com.sky.server.mvc.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
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
  private long createTime = System.currentTimeMillis();

  @Column(length = Integer.MAX_VALUE)
  private byte[] jarFile;

  private String jarFileName;
  private String arguments;

  @OneToMany(mappedBy = "project")
  private List<Work> works = new ArrayList<Work>();

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

  @Override
  public String toString() {
    return "Project{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", createTime=" + createTime +
        ", jarFile=" + Arrays.toString(jarFile) +
        ", jarFileName='" + jarFileName + '\'' +
        ", arguments='" + arguments + '\'' +
        ", owner=" + owner +
        '}';
  }

  public List<Work> getWorks() {
    return works;
  }

  public void setWorks(List<Work> works) {
    this.works = works;
  }
}
