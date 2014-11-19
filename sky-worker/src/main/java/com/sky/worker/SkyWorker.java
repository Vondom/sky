package com.sky.worker;

import com.sky.commons.WorkerControlService;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.system.ApplicationPidListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jcooky on 2014. 7. 30..
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SkyWorker implements CommandLineRunner {
  private static final Logger logger = LoggerFactory.getLogger(SkyWorker.class);
  public static final String PROFILER_PATH = FileUtils.getTempDirectoryPath() + "/sky-profiler.jar";
  private static final String PROFILER_URL = "/sky-profiler.jar";

  @Autowired
  private com.sky.worker.Options options;

  @Autowired
  private Worker worker;

  public void run(String[] args) throws ParseException, IOException, TException {

    options.init(args);
    if (!options.has(Options.Key.HELP)) {
      int port = Integer.parseInt(options.get(Options.Key.PORT));
      String host = options.get(Options.Key.HOST);

      this.downloadProfiler(host);

      WorkerControlService.Iface workerControlService = init(host);
      worker.setId(workerControlService.add("0.0.0.0", port));
      worker.setWorkerControlService(workerControlService);

      TNonblockingServerTransport transport = new TNonblockingServerSocket(new InetSocketAddress("0.0.0.0", port));
      THsHaServer server = new THsHaServer(new THsHaServer.Args(transport)
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

  public void downloadProfiler(String host) throws IOException {
    File profilerFile = new File(PROFILER_PATH);
    URL profilerUrl = getProfilerUrl(host);
    logger.trace("profilerUrl={}", profilerUrl);
    FileUtils.copyURLToFile(profilerUrl, profilerFile);
    logger.debug("FINISH Download profiler: {}", profilerFile.getAbsolutePath());
  }

  private WorkerControlService.Iface init(String host) throws TTransportException {
    if (!host.startsWith("http://"))
      host = "http://" + host;

    THttpClient httpClient = new THttpClient(host + "/api/thrift");

    return new WorkerControlService.Client(new TMultiplexedProtocol(new TBinaryProtocol(httpClient), "worker-control"));
  }

  public URL getProfilerUrl(String host) throws MalformedURLException {
    return new URL("http://" + host + PROFILER_URL);
  }

  public static void main(String[] args) throws Exception {
    String location = System.getProperty("spring.config.location");
    new SpringApplicationBuilder()
        .showBanner(true)
        .web(false)
        .addCommandLineProperties(false)
        .sources(SkyWorker.class)
        .properties("spring.config.location=" + location)
        .listeners(new ApplicationPidListener("sky-worker.pid"))
        .run(args);
  }
}
