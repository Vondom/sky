package com.sky.commons.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by jcooky on 2014. 6. 25..
 */
@Entity
//@Table(uniqueConstraints = {
//    @UniqueConstraint(name = "method_key_uk1", columnNames = {"name", "signature"})
//})
public class MethodKey {
  @Id
  @GeneratedValue
  private long id;

  private String name;
  private String signature;

  @ManyToOne
  private ClassKey classKey;

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

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public ClassKey getClassKey() {
    return classKey;
  }

  public void setClassKey(ClassKey classKey) {
    this.classKey = classKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MethodKey methodKey = (MethodKey) o;

    if (id != methodKey.id) return false;
    if (classKey != null ? !classKey.equals(methodKey.classKey) : methodKey.classKey != null) return false;
    if (name != null ? !name.equals(methodKey.name) : methodKey.name != null) return false;
    if (signature != null ? !signature.equals(methodKey.signature) : methodKey.signature != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (signature != null ? signature.hashCode() : 0);
    result = 31 * result + (classKey != null ? classKey.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "MethodKey{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", signature='" + signature + '\'' +
        '}';
  }
}
