package com.sky.profiler.jrat.provider;

import com.sky.commons.*;
import com.sky.profiler.api.SkyAPI;
import com.sky.profiler.thrift.AgentControlServiceQueue;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;
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

  private AgentControlServiceQueue collector;
  private long workId;

  public TreeMethodHandlerFactory() throws TTransportException, IOException {
    super();

    THttpClient httpClient = new THttpClient(SkyAPI.SKY_SERVER_URL);
    httpClient.setConnectTimeout(Integer.MAX_VALUE);
    httpClient.setReadTimeout(Integer.MAX_VALUE);
    collector = new AgentControlServiceQueue(new AgentControlService.Client(new TBinaryProtocol(httpClient)));
  }

  @Override
  public void startup(RuntimeContext context) throws Exception {
    LOG.info("new");
    super.startup(context);

    collector.start();
  }

  @Override
  public void shutdown() {
    collector.finish();
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

  public KThrowable createKThrowable(Throwable throwable) {
    return new KThrowable()
        .setClassKey(createKClass(throwable.getClass().getPackage().getName(), throwable.getClass().getSimpleName()))
        .setMessage(throwable.getMessage());
  }

  public KMethod createKMethod(MethodKey methodKey) {
    return new KMethod(createKClass(methodKey.getPackageName(), methodKey.getClassName()),
        methodKey.getMethodName(), methodKey.getSignature());
  }

  public KClass createKClass(String packageName, String className) {
    return new KClass(packageName, className);
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

  public AgentControlService.Iface getCollector() {
    return collector;
  }

  public synchronized MethodProfile createMethodProfile(TreeNode currentNode, long duration, String threadName, long time) {
    return new MethodProfile().setWorkId(this.getWorkId())
        .setIndex(this.getTreeNodes().indexOf(currentNode))
        .setCaller(currentNode.getParentNode().getMethodKey() == null ? null : this.createKMethod(currentNode.getParentNode().getMethodKey()))
        .setCallee(this.createKMethod(currentNode.getMethodKey()))
        .setElapsedTime(duration)
        .setThreadName(threadName)
        .setThrowable(null)
        .setTimestamp(time);
  }
}
