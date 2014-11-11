package com.sky.profiler.jrat.provider;

import com.sky.commons.MethodProfile;
import org.shiftone.jrat.core.MethodKey;

public class MethodTreeNode {
  private MethodTreeNode parent;
  private MethodKey methodKey;
  private MethodProfile profile;

  public MethodTreeNode() {

  }

  public MethodTreeNode(MethodKey methodKey, MethodTreeNode parent) {
    this.methodKey = methodKey;
    this.parent = parent;
    this.profile = new MethodProfile();
  }

  public MethodKey getMethodKey() {
    return methodKey;
  }

  public MethodProfile getProfile() {
    return profile;
  }

  public boolean isRootNode() { return methodKey == null; }
}