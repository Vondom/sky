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

    int port = Integer.parseInt(options.get(Options.Key.PORT));
    String host = options.get(Options.Key.SERVER_URL);

    this.downloadProfiler(host);

    WorkerControlService.Iface workerControlService = getWorkerControlService(host);
    worker.setId(workerControlService.add(options.get(Options.Key.LOCAL_HOST), port));
    worker.setWorkerControlService(workerControlService);

    TNonblockingServerTransport transport = new TNonblockingServerSocket(new InetSocketAddress("0.0.0.0", port));
    THsHaServer server = new THsHaServer(new THsHaServer.Args(transport)
        .protocolFactory(new TCompactProtocol.Factory())
        .processor(new com.sky.commons.Worker.Processor<Worker>(worker)));
    server.serve();

    workerControlService.remove(worker.getId());

  }

  public void downloadProfiler(String url) throws IOException {
    File profilerFile = new File(PROFILER_PATH);
    URL profilerUrl = getProfilerUrl(url);

    logger.trace("profilerUrl={}", profilerUrl);
    FileUtils.copyURLToFile(profilerUrl, profilerFile);
    logger.debug("FINISH Download profiler: {}", profilerFile.getAbsolutePath());
  }

  private WorkerControlService.Iface getWorkerControlService(String url) throws TTransportException {

    THttpClient httpClient = new THttpClient(url + "/api/thrift");

    return new WorkerControlService.Client(new TMultiplexedProtocol(new TBinaryProtocol(httpClient), "worker-control"));
  }

  public URL getProfilerUrl(String url) throws MalformedURLException {
    return new URL(url + PROFILER_URL);
  }

  public static void main(String[] args) throws Exception {
    new SpringApplicationBuilder()
        .showBanner(true)
        .web(false)
        .addCommandLineProperties(true)
        .sources(SkyWorker.class)
        .listeners(new ApplicationPidListener("sky-worker.pid"))
        .run(args);
  }
}
