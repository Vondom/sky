package com.sky.worker.config;

import com.sky.commons.Worker;
import com.sky.commons.WorkerControlService;
import com.sky.worker.Options;
import com.sky.worker.service.Runner;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by jcooky on 2014. 7. 31..
 */
public class WorkerConfig {
  // TODO
//  public static final String SKY_PROFILER_JAR_PATH = FileUtils.getTempDirectoryPath() + "/sky-profiler.jar";
  public static final String SKY_PROFILER_JAR_PATH = "/Users/jcooky/IdeaProjects/sky/sky-profiler/target/sky-profiler.jar";
  public static final String SKY_REGISTER_URL = "/agent/work-manager";

  private WorkerControlService.Iface workManager;
  private Runner runner;
  private Worker.Iface worker;

  public WorkerControlService.Iface workManager(Options options) throws TTransportException {

    return this.workManager = new WorkerControlService.Client(new TCompactProtocol(new THttpClient(options.get(Options.Key.HOST)+SKY_REGISTER_URL)));
  }

  public Runner runner(Options options) {
    return this.runner = new Runner(options);
  }

  public Worker.Iface worker(Runner runner, WorkerControlService.Iface workerControlService, Options options) {
    return this.worker = new com.sky.worker.service.Worker(runner, workerControlService, options);
  }

  public Worker.Iface worker(Options options) throws TTransportException {
    return worker(runner(options), workManager(options), options);
  }
}
