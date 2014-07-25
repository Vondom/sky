package com.sky.server.mvc.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jcooky on 2014. 7. 21..
 */
@Entity
public class User {
  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true)
  private String email;

  @OneToMany(mappedBy = "owner")
  private Set<Project> projects = new HashSet<Project>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<Project> getProjects() {
    return projects;
  }
}
