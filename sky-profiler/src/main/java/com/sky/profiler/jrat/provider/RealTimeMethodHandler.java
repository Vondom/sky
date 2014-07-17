package com.sky.profiler.jrat.provider;

import com.sky.commons.RealtimeMethodProfile;
import com.sky.profiler.api.SkyAPI;
import org.shiftone.jrat.core.MethodKey;
import org.shiftone.jrat.core.spi.MethodHandler;
import org.shiftone.jrat.util.log.Logger;

import java.io.IOException;

/**
 * Created by jcooky on 2014. 6. 19..
 */
public class RealTimeMethodHandler implements MethodHandler {
  private static final Logger LOGGER = Logger.getLogger(RealTimeMethodHandler.class);

  RealtimeMethodProfile profile = new RealtimeMethodProfile();

  public RealTimeMethodHandler(long profileId, MethodKey methodKey, MethodKey caller, long index) {
    profile.setMethod(methodKey);
    profile.setCaller(caller);
    profile.setIndex(index);
    profile.setProfileId(profileId);
  }

  @Override
  public void onMethodStart() {
    profile.setTimestamp(System.currentTimeMillis());
  }

  @Override
  public void onMethodFinish(long durationMillis, Throwable throwable) {
    profile.setElapsedTime(durationMillis);
    profile.setThrowable(throwable);
    profile.setThreadName(Thread.currentThread().getName());

    try {
      SkyAPI.put(profile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
