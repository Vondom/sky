package com.sky.profiler.jrat.provider;

import org.shiftone.jrat.core.Accumulator;
import org.shiftone.jrat.core.MethodKey;
import org.shiftone.jrat.util.log.Logger;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class TreeNode
 *
 * @author jeff@shiftone.org (Jeff Drost)
 */
public class TreeNode implements Externalizable {

  private static final Logger LOG = Logger.getLogger(TreeNode.class);
  private static final long serialVersionUID = 1;
  protected MethodKey methodKey;
  protected TreeNode parent;
  private Accumulator accumulator;
  protected final Map<MethodKey, TreeNode> children = new HashMap<MethodKey, TreeNode>(5);
//  private MethodProfile methodProfile = new MethodProfile();

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    // LOG.info("Writing node for " + methodKey);
    out.writeObject(accumulator);
    out.writeObject(methodKey);

    // column a copy of the children
    List<? extends TreeNode> children = getChildren();

    // write a child count
    out.writeInt(children.size());
    // write the children
    for (TreeNode child : children) {
      child.writeExternal(out);
    }
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    this.accumulator = (Accumulator) in.readObject();
    this.methodKey = (MethodKey) in.readObject();

    int childCount = in.readInt();
    for (int i = 0; i < childCount; i++) {
      TreeNode child = new TreeNode();
      child.readExternal(in);
      children.put(child.getMethodKey(), child);
      child.parent = this;
    }
  }

  public TreeNode() {
    // root node
    this.methodKey = null;
    this.parent = null;
    this.accumulator = new Accumulator();
  }

  public TreeNode(MethodKey methodKey, TreeNode treeNode) {
    this.methodKey = methodKey;
    this.parent = treeNode;
    this.accumulator = new Accumulator();
  }

  public List<? extends TreeNode> getChildren() {
    synchronized (children) {
      return new ArrayList<TreeNode>(children.values());
    }
  }

  /**
   * Method gets <b>AND CREATES IF NEEDED</b> the requested tree node
   */
  public TreeNode getChild(TreeMethodHandlerFactory factory, MethodKey methodKey) {
    synchronized (children) {
      TreeNode treeNode = children.get(methodKey);

      if (treeNode == null) {
        treeNode = factory.createTreeNode(methodKey, this);
        // LOG.info("Created node for " + methodKey);
        children.put(methodKey, treeNode);
      }

      return treeNode;
    }
  }

  public MethodKey getMethodKey() {
    return methodKey;
  }

  public final boolean isRootNode() {
    return (methodKey == null);
  }

  public final TreeNode getParentNode() {
    return parent;
  }

  public Accumulator getAccumulator() {
    return accumulator;
  }

  // ---------------------------------------------------------------
  public void reset() {

    // need to clone map - concurrency issues
    List<TreeNode> list = new ArrayList<TreeNode>();

    synchronized (children) {
      list.addAll(children.values());
    }

    for (TreeNode treeNode : list) {
      treeNode.reset();
    }

    accumulator.reset();  // this is the actual call to reset
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + ": " + getMethodKey() + " -> " + getAccumulator();
  }

}
