package com.sky.server.mvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by jcooky on 2014. 6. 25..
 */
@Entity
public class Profile {
  @Id
  @GeneratedValue
  private long id;

  @OneToMany
  private Set<MethodLog> methodLogs;

  @OneToOne(mappedBy = "profile")
  @JsonIgnore
  private Work work;

  public void setId(long id) {
    this.id = id;
  }

  public Set<MethodLog> getMethodLogs() {
    return methodLogs;
  }

  public void setMethodLogs(Set<MethodLog> methodLogs) {
    this.methodLogs = methodLogs;
  }

  public long getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Profile profile = (Profile) o;

    if (id != profile.id) return false;
    if (methodLogs != null ? !methodLogs.equals(profile.methodLogs) : profile.methodLogs != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (methodLogs != null ? methodLogs.hashCode() : 0);
    return result;
  }

  public Work getWork() {
    return work;
  }

  public void setWork(Work work) {
    this.work = work;
  }
}
