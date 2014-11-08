package com.sky.profiler.jrat.provider;

import com.sky.commons.*;
import com.sky.profiler.api.SkyAPI;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;
import org.shiftone.jrat.core.MethodKey;
import org.shiftone.jrat.core.spi.AbstractMethodHandlerFactory;
import org.shiftone.jrat.core.spi.MethodHandler;
import org.shiftone.jrat.core.spi.RuntimeContext;
import org.shiftone.jrat.util.log.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jcooky on 2014. 6. 18..
 */
public class RealTimeMethodHandlerFactory extends AbstractMethodHandlerFactory implements RealTimeMethodHandlerFactoryMBean {
  private static final Logger logger = Logger.getLogger(RealTimeMethodHandlerFactory.class);

  private ThreadLocal<List<MethodTreeNode>> methodKeys = new ThreadLocal<List<MethodTreeNode>>();

  private AgentControlService.Iface collector;
  private Long workId;

  public RealTimeMethodHandlerFactory() throws TTransportException {
    super();
    THttpClient httpClient = new THttpClient(SkyAPI.SKY_SERVER_URL);
    httpClient.setConnectTimeout(Integer.MAX_VALUE);
    httpClient.setReadTimeout(Integer.MAX_VALUE);
    collector = new AgentControlService.Client(new TCompactProtocol(httpClient));
  }

  public void startup(RuntimeContext context) throws Exception {
    super.startup(context);

    methodKeys.set(new LinkedList<MethodTreeNode>());
  }

  @Override
  public void shutdown() {
    super.shutdown();
  }

  @Override
  public synchronized MethodHandler createMethodHandler(MethodKey methodKey) {
    final List<MethodTreeNode> mk = methodKeys.get();

    final MethodTreeNode node = new MethodTreeNode(mk.size(), methodKey, new MethodProfile()),
        parentNode = mk.isEmpty() ? null : mk.get(mk.size() - 1);

    mk.add(node);

    node.getProfile().setCallee(toKMethod(methodKey))
      .setCaller(parentNode != null ? toKMethod(parentNode.getMethodKey()) : null)
      .setIndex(node.getIndex())
      .setWorkId(workId)
      .setElapsedTime(0L);

    return new MethodHandler() {
      private MethodProfile profile = node.getProfile();

      @Override
      public void onMethodStart() {
        profile.setTimestamp(System.currentTimeMillis());
      }

      @Override
      public void onMethodFinish(long durationMillis, Throwable throwable) {
        if (parentNode != null) {
          parentNode.getProfile().setElapsedTime(parentNode.getProfile().getElapsedTime() + durationMillis);
        }

        try {
          collector.put(profile.setElapsedTime(durationMillis - profile.getElapsedTime())
              .setTotalElapsedTime(durationMillis)
              .setThrowable(throwable != null ? toKThrowable(throwable) : null)
              .setThreadName(Thread.currentThread().getName()));
        } catch (TException e) {
          e.printStackTrace();
        } finally {
          mk.remove(node.getIndex());
        }
      }
    };

  }

  private KThrowable toKThrowable(Throwable throwable) {
    return new KThrowable()
        .setClassKey(toKClass(throwable.getClass().getPackage().getName(), throwable.getClass().getSimpleName()))
        .setMessage(throwable.getMessage());
  }

  private KMethod toKMethod(MethodKey methodKey) {
    return new KMethod(toKClass(methodKey.getPackageName(), methodKey.getClassName()),
        methodKey.getMethodName(), methodKey.getSignature());
  }

  private KClass toKClass(String packageName, String className) {
    return new KClass(packageName, className);
  }

  @Override
  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  @Override
  public Long getWorkId() {
    return this.workId;
  }

  public static class MethodTreeNode {
    private int index;
    private MethodKey methodKey;
    private MethodProfile profile;

    public MethodTreeNode(int index, MethodKey methodKey, MethodProfile profile) {
      this.index = index;
      this.methodKey = methodKey;
      this.profile = profile;
    }

    public MethodKey getMethodKey() {
      return methodKey;
    }

    public MethodProfile getProfile() {
      return profile;
    }

    public int getIndex() {
      return index;
    }

  }
}
