package com.sky.profiler.thrift;

import com.sky.commons.AgentControlService;
import com.sky.commons.MethodProfile;
import org.apache.thrift.TException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jcooky on 14. 11. 11..
 */
public class AgentControlServiceQueue implements Runnable, AgentControlService.Iface {
  private AgentControlService.Iface iface;

  private Thread thread = new Thread(this, "sender-thread");
  private BlockingQueue<MethodProfile> q = new LinkedBlockingQueue<MethodProfile>();
  private boolean stop = false;

  public AgentControlServiceQueue(AgentControlService.Iface iface) {
    this.iface = iface;
  }


  public void put(MethodProfile methodProfile) {
    try {
      q.put(methodProfile);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    try {
      while (!stop || !q.isEmpty()) {
        iface.put(q.take());
      }

    } catch (TException e) {
      e.printStackTrace();
    } catch (InterruptedException ignored) {
    }
  }

  public void start() {
    this.stop = false;
    thread.setDaemon(true);

    thread.start();
  }

  public void finish() {
    this.stop = true;
    if (Thread.State.WAITING.equals(thread.getState()))
      thread.interrupt();
  }
}
