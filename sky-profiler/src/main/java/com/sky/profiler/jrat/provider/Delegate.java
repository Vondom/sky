package com.sky.profiler.jrat.provider;

import org.apache.thrift.TException;
import org.shiftone.jrat.core.MethodKey;
import org.shiftone.jrat.util.Assert;

/**
 * This is basically a thread specific MethodHandler. The typical JRat model is
 * to have a seperate handler for each method. This is also true for the
 * TreeMethodHandler, however that handler delegates to an instance of this
 * class, passing it the method key with each invocation. One instance of this
 * class will exist for each thread that is creating JRat events. This class
 * manipulates a tree structure as invocations are made.
 *
 * @author jeff@shiftone.org (Jeff Drost)
 */
public class Delegate {

  private final TreeMethodHandlerFactory factory;
  private TreeNode currentNode;

  public Delegate(TreeMethodHandlerFactory factory) {
    Assert.assertNotNull(factory);
    this.factory = factory;
    this.currentNode = factory.getRootNode();
  }

  public final void onMethodStart(MethodKey methodKey) {
    currentNode = currentNode.getChild(factory, methodKey);
    currentNode.getAccumulator().onMethodStart();
  }

  public final void onMethodFinish(MethodKey methodKey, long duration, boolean success) {
    try {
      factory.getCollector().put(factory.createMethodProfile(currentNode, duration, Thread.currentThread().getName(), System.currentTimeMillis()));
    } catch (TException e) {
      e.printStackTrace();
    }

    currentNode.getAccumulator().onMethodFinish(duration, success);
    currentNode = currentNode.getParentNode();
  }
}
