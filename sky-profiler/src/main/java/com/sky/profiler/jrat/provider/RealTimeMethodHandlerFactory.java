//package com.sky.profiler.jrat.provider;
//
//import com.sky.commons.*;
//import com.sky.profiler.api.SkyAPI;
//import com.sky.profiler.thrift.AgentControlServiceQueue;
//import org.apache.thrift.protocol.TCompactProtocol;
//import org.apache.thrift.transport.THttpClient;
//import org.apache.thrift.transport.TTransportException;
//import org.shiftone.jrat.core.MethodKey;
//import org.shiftone.jrat.core.spi.AbstractMethodHandlerFactory;
//import org.shiftone.jrat.core.spi.MethodHandler;
//import org.shiftone.jrat.core.spi.RuntimeContext;
//import org.shiftone.jrat.util.log.Logger;
//
///**
// * Created by jcooky on 2014. 6. 18..
// */
//public class RealTimeMethodHandlerFactory extends AbstractMethodHandlerFactory implements TreeMethodHandlerFactoryMBean {
//  private static final Logger logger = Logger.getLogger(RealTimeMethodHandlerFactory.class);
//
//  private MethodTreeNode rootNode = new MethodTreeNode();
//
//  private AgentControlServiceQueue collector;
//  private Long workId;
//
//  public RealTimeMethodHandlerFactory() throws TTransportException {
//    super();
//    THttpClient httpClient = new THttpClient(SkyAPI.SKY_SERVER_URL);
//    httpClient.setConnectTimeout(Integer.MAX_VALUE);
//    httpClient.setReadTimeout(Integer.MAX_VALUE);
//    collector = new AgentControlServiceQueue(new AgentControlService.Client(new TCompactProtocol(httpClient)));
//  }
//
//  public void startup(RuntimeContext context) throws Exception {
//    super.startup(context);
//
//    collector.start();
//  }
//
//  @Override
//  public void shutdown() {
//    collector.finish();
//
//    super.shutdown();
//  }
//
//  public MethodTreeNode createMethodTreeNode(MethodKey methodKey, MethodTreeNode treeNode) {
//    return new MethodTreeNode(methodKey, treeNode);
//  }
//
//  @Override
//  public synchronized MethodHandler createMethodHandler(MethodKey methodKey) {
//    return new RealTimeMethodHandler(node);
//
//  }
//
//  private KThrowable createKThrowable(Throwable throwable) {
//    return new KThrowable()
//        .setClassKey(createKClass(throwable.getClass().getPackage().getName(), throwable.getClass().getSimpleName()))
//        .setMessage(throwable.getMessage());
//  }
//
//  private KMethod createKMethod(MethodKey methodKey) {
//    return new KMethod(createKClass(methodKey.getPackageName(), methodKey.getClassName()),
//        methodKey.getMethodName(), methodKey.getSignature());
//  }
//
//  private KClass createKClass(String packageName, String className) {
//    return new KClass(packageName, className);
//  }
//
//  @Override
//  public void setWorkId(Long workId) {
//    this.workId = workId;
//  }
//
//  @Override
//  public Long getWorkId() {
//    return this.workId;
//  }
//
//
//}
