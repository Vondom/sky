package com.sky.profiler.jrat.provider;

import com.sky.profiler.api.SkyAPI;
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
public class RealTimeMethodHandlerFactory extends AbstractMethodHandlerFactory {
  private static final Logger logger = Logger.getLogger(RealTimeMethodHandlerFactory.class);

  private ThreadLocal<List<MethodKey>> methodKeys = new ThreadLocal<List<MethodKey>>();
  private ThreadLocal<Long> profileId = new ThreadLocal<Long>();

  public void startup(RuntimeContext context) throws Exception {
    super.startup(context);

    profileId.set(SkyAPI.create());
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

    return new RealTimeMethodHandler(profileId.get(), methodKey, caller, mk.size()-1);
  }
}
