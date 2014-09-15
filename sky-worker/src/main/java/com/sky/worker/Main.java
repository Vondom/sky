package com.sky.worker;

import com.sky.commons.WorkerControlService;
import org.apache.commons.cli.ParseException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by jcooky on 2014. 7. 30..
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Main implements CommandLineRunner {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  @Autowired
  private com.sky.worker.Options options;

  @Autowired
  private Worker worker;

  public void run(String[] args) throws ParseException, IOException, TException {

    options.init(args);
    if (!options.has(Options.Key.HELP)) {
      int port = Integer.parseInt(options.get(Options.Key.PORT));
      String host = options.get(Options.Key.HOST);

      WorkerControlService.Iface workerControlService = init(host);
      worker.setId(workerControlService.add("0.0.0.0", port));

      TNonblockingServerTransport transport = new TNonblockingServerSocket(new InetSocketAddress("0.0.0.0", port));
      TNonblockingServer server = new TNonblockingServer(new THsHaServer.Args(transport)
          .protocolFactory(new TCompactProtocol.Factory())
          .processor(new com.sky.commons.Worker.Processor<Worker>(worker)));
      server.serve();

      workerControlService.remove(worker.getId());
    } else {
      System.out.println();
      options.printHelp();
      System.out.println();
    }
  }

  private WorkerControlService.Iface init(String host) throws TTransportException {
    if (!host.startsWith("http://"))
      host = "http://" + host;

    THttpClient httpClient = new THttpClient(host + "/agent/worker-control");
    WorkerControlService.Iface workerControlService = new WorkerControlService.Client(new TCompactProtocol(httpClient));

    return workerControlService;
  }

  public static void main(String[] args) throws Exception {
    new SpringApplicationBuilder()
        .showBanner(false)
        .addCommandLineProperties(false)
        .sources(Main.class)
        .run(args);
  }
}
