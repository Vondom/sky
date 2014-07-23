package com.sky.server.mvc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by jcooky on 2014. 7. 21..
 */
@Entity
public class Project {
  @Id
  @GeneratedValue
  private long id;

  private String name;

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
}
