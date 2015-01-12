package com.sky.profiler.jrat.provider;

import com.sky.commons.domain.ClassKey;
import com.sky.commons.domain.MethodLog;
import com.sky.commons.domain.Work;
import com.sky.profiler.Keys;
import com.sky.profiler.rest.MethodLogSender;
import org.shiftone.jrat.core.MethodKey;
import org.shiftone.jrat.core.spi.AbstractMethodHandlerFactory;
import org.shiftone.jrat.core.spi.MethodHandler;
import org.shiftone.jrat.core.spi.RuntimeContext;
import org.shiftone.jrat.util.log.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class TreeMethodHandlerFactory
 *
 * @author jeff@shiftone.org (Jeff Drost)
 */
public class TreeMethodHandlerFactory extends AbstractMethodHandlerFactory implements TreeMethodHandlerFactoryMBean {

  private static final Logger LOG = Logger.getLogger(TreeMethodHandlerFactory.class);
  private final TreeNode rootNode = new TreeNode();
  private final Set<MethodKey> allMethodKeys = new HashSet<MethodKey>();
  private final DelegateThreadLocal delegateThreadLocal = new DelegateThreadLocal(this);
  private final List<TreeNode> treeNodes = new ArrayList<TreeNode>();

  private final MethodLogSender sender;

  private long workId;

  public TreeMethodHandlerFactory() throws IOException {
    super();

    sender = new MethodLogSender();
  }

  @Override
  public void startup(RuntimeContext context) throws Exception {
    LOG.info("new");
    super.startup(context);

    sender.start();
  }

  @Override
  public void shutdown() {
    sender.finish();
  }

  @Override
  public synchronized final MethodHandler createMethodHandler(MethodKey methodKey) {

    allMethodKeys.add(methodKey);

    return new TreeMethodHandler(this, methodKey);
  }

  public TreeNode createTreeNode(MethodKey methodKey, TreeNode treeNode) {
    TreeNode newNode = new TreeNode(methodKey, treeNode);
    // this add makes the web factory able to look up a node by id.
    treeNodes.add(treeNode);
    return newNode;
  }

  public com.sky.commons.domain.MethodKey createKMethod(MethodKey methodKey) {
    com.sky.commons.domain.MethodKey mk = new com.sky.commons.domain.MethodKey();
    mk.setClassKey(createKClass(methodKey.getClassName(), methodKey.getPackageName()));
    mk.setName(methodKey.getMethodName());
    mk.setSignature(methodKey.getSignature());

    return mk;
  }

  public ClassKey createKClass(String packageName, String className) {
    return new ClassKey(packageName, className);
  }


  /**
   * Returns the current thread's delegate instance. This delegate will
   * operate on this factory's call tree data structure when events are
   * processed.
   */
  public final Delegate getDelegate() {
    return delegateThreadLocal.get();
  }

  public final TreeNode getRootNode() {
    return rootNode;
  }

  public final List<TreeNode> getTreeNodes() {
    return treeNodes;
  }

  public synchronized void reset() {
    rootNode.reset();
  }

  @Override
  public String toString() {
    return "Tree Handler Factory";
  }

  @Override
  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  @Override
  public Long getWorkId() {
    return workId;
  }

  public synchronized MethodLog createMethodProfile(TreeNode currentNode, long duration, String threadName, long startTime) {
    Work work = new Work();
    work.setId(workId);


    MethodLog methodLog = new MethodLog();
    methodLog.setOrdering(currentNode.getDepth());
    methodLog.setCaller(currentNode.getParentNode().getMethodKey() == null ? null : this.createKMethod(currentNode.getParentNode().getMethodKey()));
    methodLog.setMethodKey(this.createKMethod(currentNode.getMethodKey()));
    methodLog.setElapsedTime(duration);
    methodLog.setThreadName(threadName);
    methodLog.setStartTime(startTime);
    methodLog.setWork(work);

    return methodLog;
  }

  public void putMethodLog(MethodLog methodLog) {
    this.sender.send(System.getProperty(Keys.SERVER_CONNECTION_KEY) + "/api/method-log", methodLog);
  }
}
