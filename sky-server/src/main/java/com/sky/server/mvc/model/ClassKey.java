package com.sky.server.mvc.model;

import javax.persistence.*;

/**
 * Created by jcooky on 2014. 6. 25..
 */
@Entity
//@Table(uniqueConstraints = {
//    @UniqueConstraint(name = "class_key_uk1", columnNames = {"name", "packageName"})
//})
public class ClassKey {
  @Id
  @GeneratedValue
  private long id;

  private String name;
  private String packageName;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ClassKey classKey = (ClassKey) o;

    if (id != classKey.id) return false;
    if (name != null ? !name.equals(classKey.name) : classKey.name != null) return false;
    if (packageName != null ? !packageName.equals(classKey.packageName) : classKey.packageName != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
    return result;
  }
}
