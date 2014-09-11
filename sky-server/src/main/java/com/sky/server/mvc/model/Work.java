package com.sky.server.mvc.model;


import javax.persistence.*;

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

  @ManyToOne
  private Project project;

  @ManyToOne
  private Worker worker;

  @OneToOne
  private Profile profile;

  public Worker getWorker() {
    return worker;
  }

  public void setWorker(Worker worker) {
    this.worker = worker;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
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

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }
}
