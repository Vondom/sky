package com.sky.commons;

import org.shiftone.jrat.core.MethodKey;

import java.io.Serializable;

/**
 * Created by jcooky on 2014. 7. 7..
 */
public class RealtimeMethodProfile implements Serializable {
  private MethodKey method;
  private MethodKey caller;
  private Long elapsedTime, profileId, timestamp;
  private Throwable throwable = null;
  private long index;
  private String threadName;

  public Long getProfileId() {
    return profileId;
  }

  public void setProfileId(Long profileId) {
    this.profileId = profileId;
  }

  public MethodKey getMethod() {
    return method;
  }

  public void setMethod(MethodKey method) {
    this.method = method;
  }

  public MethodKey getCaller() {
    return caller;
  }

  public void setCaller(MethodKey caller) {
    this.caller = caller;
  }

  public Long getElapsedTime() {
    return elapsedTime;
  }

  public void setElapsedTime(Long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RealtimeMethodProfile that = (RealtimeMethodProfile) o;

    if (caller != null ? !caller.equals(that.caller) : that.caller != null) return false;
    if (elapsedTime != null ? !elapsedTime.equals(that.elapsedTime) : that.elapsedTime != null) return false;
    if (method != null ? !method.equals(that.method) : that.method != null) return false;
    if (throwable != null ? !throwable.equals(that.throwable) : that.throwable != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = method != null ? method.hashCode() : 0;
    result = 31 * result + (caller != null ? caller.hashCode() : 0);
    result = 31 * result + (elapsedTime != null ? elapsedTime.hashCode() : 0);
    result = 31 * result + (throwable != null ? throwable.hashCode() : 0);
    return result;
  }

  public long getIndex() {
    return index;
  }

  public void setIndex(long index) {
    this.index = index;
  }

  public String getThreadName() {
    return threadName;
  }

  public void setThreadName(String threadName) {
    this.threadName = threadName;
  }
}
