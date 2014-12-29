package com.sky.commons.model;

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
  private String imageUrl;
  private String name;

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

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
