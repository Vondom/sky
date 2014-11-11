//package com.sky.profiler.jrat.provider;
//
//import com.sky.commons.MethodProfile;
//import org.shiftone.jrat.core.spi.MethodHandler;
//
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * Created by jcooky on 14. 11. 12..
// */
//public class RealTimeMethodHandler implements MethodHandler {
//  private final MethodTreeNode node;
//  private MethodProfile profile = node.getProfile();
//
//  public RealTimeMethodHandler(MethodTreeNode node) {
//    this.node = node;
//  }
//
//  private List<MethodTreeNode> get() {
//    List<MethodTreeNode> mk = methodKeys.get();
//    if (mk == null) {
//      mk = new LinkedList<MethodTreeNode>();
//      methodKeys.set(mk);
//    }
//
//    return mk;
//  }
//
//  @Override
//  public void onMethodStart() {
//    List<MethodTreeNode> mk = get();
//    MethodTreeNode parentNode = mk.isEmpty() ? null : mk.get(mk.size() - 1);
//
//    profile.setIndex(mk.size());
//    profile.setCaller(parentNode != null ? toKMethod(parentNode.getMethodKey()) : null);
//    profile.setTimestamp(System.currentTimeMillis());
//
//    mk.add(node);
//  }
//
//  @Override
//  public void onMethodFinish(long durationMillis, Throwable throwable) {
//    List<MethodTreeNode> mk = get();
//
//    try {
//      collector.put(profile.setElapsedTime(durationMillis)
//          .setThrowable(throwable != null ? toKThrowable(throwable) : null)
//          .setThreadName(Thread.currentThread().getName()));
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    } finally {
//      mk.remove(node);
//    }
//  }
//}
