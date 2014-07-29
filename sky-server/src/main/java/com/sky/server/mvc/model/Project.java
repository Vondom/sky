package com.sky.server.mvc.model;

import javax.persistence.*;

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
  private byte[] jarFile;
  private String arguments;

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
}
