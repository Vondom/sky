package com.sky.server;

import com.sky.commons.WorkerControlService;
import com.sky.server.mvc.model.Project;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.repository.WorkerRepository;
import com.sky.server.mvc.service.WorkerService;
import com.sky.server.test.SpringBasedTestSupport;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Future;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by jcooky on 2014. 9. 15..
 */
public class WorkerModuleTest extends SpringBasedTestSupport {

  @Autowired
  private WorkerControlService.Iface workerControlService;

  @Autowired
  private WorkerService workerService;

  @Autowired
  private WorkerRepository workerRepository;

  @Autowired
  private WorkRepository workRepository;

  @After
  public void tearDown() throws Exception {
    workerRepository.deleteAll();
    workRepository.deleteAll();
  }

  @Test
  public void testAddWorkerAndDoWork() throws Exception {
    com.sky.commons.Worker.Iface worker = mock(com.sky.commons.Worker.Iface.class);
    doReturn("Output").when(worker).doWork(any(com.sky.commons.Work.class));

    THsHaServer server = null;
    try {
      TNonblockingServerSocket socket = new TNonblockingServerSocket(9090);
      final THsHaServer server2 = new THsHaServer(new THsHaServer.Args(socket)
          .processor(new com.sky.commons.Worker.Processor<com.sky.commons.Worker.Iface>(worker))
          .protocolFactory(new TCompactProtocol.Factory()));
      server = server2;
      new Thread() {
        public void run() {
          server2.serve();
        }
      }.start();

      while(!server.isServing()) Thread.sleep(1L);
      assertTrue(server.isServing());

      Project project = new Project();
      project.setId(10L);
      project.setName("test");
      project.setJarFile(new byte[]{1,1,1,1});
      project.setJarFileName("test.jar");
      project.setArguments("test1");

      Work work = new Work();
      work.setId(11L);
      work.setOrdering(1L);
      work.setProject(project);

      workerControlService.add("localhost", 9090);
      Future<Boolean> future = workerService.doWork(work);

      assertTrue(future.get());

      verify(worker).doWork(any(com.sky.commons.Work.class));

    } finally {
      if (server != null)
        server.stop();
    }
  }
}
