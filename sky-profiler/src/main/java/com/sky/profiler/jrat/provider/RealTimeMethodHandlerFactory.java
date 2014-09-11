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

  private ThreadLocal<List<MethodKey>> methodKeys = new ThreadLocal<List<MethodKey>>();
  private ThreadLocal<Long> profileId = new ThreadLocal<Long>();

  private AgentControlService.Iface collector;
  private Long projectId, workId;

  public RealTimeMethodHandlerFactory() throws TTransportException {
    super();
    THttpClient httpClient = new THttpClient(SkyAPI.SKY_SERVER_URL);
    httpClient.setConnectTimeout(Integer.MAX_VALUE);
    httpClient.setReadTimeout(Integer.MAX_VALUE);
    collector = new AgentControlService.Client(new TCompactProtocol(httpClient));
  }

  public void startup(RuntimeContext context) throws Exception {
    super.startup(context);

    profileId.set(collector.createProfile(this.workId));
    methodKeys.set(new LinkedList<MethodKey>());
  }

  @Override
  public void shutdown() {
    super.shutdown();
  }

  @Override
  public synchronized MethodHandler createMethodHandler(MethodKey methodKey) {
    List<MethodKey> mk = methodKeys.get();

    MethodKey caller = mk.isEmpty() ? null : mk.get(mk.size()-1);
    mk.add(methodKey);

    final MethodProfile profile = new MethodProfile();

    profile.setCallee(toKMethod(methodKey))
      .setCaller(caller != null ? toKMethod(caller) : null)
      .setIndex(mk.size() - 1)
      .setProfileId(profileId.get())
      .setProjectId(projectId);

    return new MethodHandler() {

      @Override
      public void onMethodStart() {
        profile.setTimestamp(System.currentTimeMillis());
      }

      @Override
      public void onMethodFinish(long durationMillis, Throwable throwable) {
        profile.setElapsedTime(durationMillis)
          .setThrowable(throwable != null ? toKThrowable(throwable) : null)
          .setThreadName(Thread.currentThread().getName());

        try {
          collector.put(profile);
        } catch (TException e) {
          e.printStackTrace();
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
  public void setProjectId(Long projectId) {
    this.projectId = projectId;
  }

  @Override
  public Long getProjectId() {
    return projectId;
  }

  @Override
  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  @Override
  public Long getWorkId() {
    return this.workId;
  }
}
