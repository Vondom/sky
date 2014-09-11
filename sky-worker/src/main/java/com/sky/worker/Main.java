package com.sky.worker;

import com.sky.commons.Worker;
import com.sky.worker.config.WorkerConfig;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * Created by jcooky on 2014. 7. 30..
 */
public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  private com.sky.worker.Options options;

  private String profilerPath = "/sky-profiler.jar";

  public Main() throws IOException {
    options = new Options();
  }

  public void run(String[] args) throws ParseException, IOException, TException {
    WorkerConfig workerConfig = new WorkerConfig();

    options.init(args);
    if (!options.has(Options.Key.HELP)) {
      setUp();

      Worker.Iface worker = workerConfig.worker(options);

      int port = Integer.parseInt(options.get(Options.Key.PORT));

      TNonblockingServerTransport transport = new TNonblockingServerSocket(new InetSocketAddress(options.get(Options.Key.HOST), port));
      TNonblockingServer server = new TNonblockingServer(new THsHaServer.Args(transport)
          .protocolFactory(new TCompactProtocol.Factory())
          .processor(new Worker.Processor<Worker.Iface>(worker)));
      server.serve();
    } else {
      System.out.println(options.toString());
    }
  }

  public void setUp() throws IOException, TException {
    FileUtils.copyURLToFile(new URL(options.get(Options.Key.HOST) + profilerPath), new File(WorkerConfig.SKY_PROFILER_JAR_PATH));
  }

  public static void main(String[] args) throws Exception {
    new Main().run(args);
  }
}
