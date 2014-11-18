package com.sky.server.mvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Project> projects = new ArrayList<Project>();

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

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) { this.projects = projects; }
}
