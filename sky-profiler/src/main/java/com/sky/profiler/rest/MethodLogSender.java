package com.sky.profiler.rest;

import com.sky.commons.model.MethodLog;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jcooky on 14. 11. 11..
 */
public class MethodLogSender {
  private RestTemplate restTemplate = new RestTemplate();
  private BlockingQueue<QueueData> q = new LinkedBlockingQueue<QueueData>();
  private boolean stop = false;

  private Thread thread;

  public MethodLogSender() {
    this.thread = new Thread("sender-thread") {
      public void run() {
        try {
          while (!stop) {
            QueueData data = q.take();
            restTemplate.postForObject(data.getUri(), data.getMethodLog(), MethodLog.class, data.getUriVariables());
          }

        } catch (InterruptedException ignored) {
        }
      }
    };
  }

  public void send(String uri, MethodLog methodLog, Object... uriVariables) {
    try {
      q.put(new QueueData(uri, methodLog, uriVariables));
    } catch (InterruptedException e) {
      e.printStackTrace();
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

  private class QueueData extends MethodLog {
    private final String uri;
    private final MethodLog methodLog;
    private final Object[] uriVariables;

    public QueueData(String uri, MethodLog methodLog, Object[] uriVariables) {
      this.uri = uri;
      this.methodLog = methodLog;
      this.uriVariables = uriVariables;
    }

    public String getUri() {
      return uri;
    }

    public MethodLog getMethodLog() {
      return methodLog;
    }

    public Object[] getUriVariables() {
      return uriVariables;
    }
  }
}
