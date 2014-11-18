package com.sky.server.mvc.service;

import com.sky.server.mvc.model.ExecutionUnit;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.repository.WorkerRepository;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class WorkerServiceTest {
  private static final Logger logger = LoggerFactory.getLogger(WorkerServiceTest.class);

  private static final int PORT = 53121;


  @InjectMocks
  private WorkerService workerService;

  @Mock
  public WorkerRepository workerRepository;

  @Mock
  public com.sky.commons.Worker.Iface mockWorker;

  @Mock
  public WorkRepository workRepository;

  private TServer server;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    TServerSocket serverSocket = new TServerSocket(PORT);
    server = new TSimpleServer(new TServer.Args(serverSocket)
                  .protocolFactory(new TCompactProtocol.Factory())
                  .processor(new com.sky.commons.Worker.Processor(mockWorker))
                  .transportFactory(new TFramedTransport.Factory()));
    new Thread() {
      public void run() {
        server.serve();
      }
    }.start();

    while(!server.isServing()) { Thread.sleep(1); }
  }

  @After
  public void tearDown() throws Exception {
    assertThat(server.isServing(), is(true));
    server.stop();
  }

  @Test
  public void testDoWork() throws Exception {
    Work work = new Work();
    work.setId(10L);
    ExecutionUnit eu = new ExecutionUnit();
    eu.setId(11L);
    eu.setJarFile(new byte[]{1,2,3,4});
    eu.setJarFileName("test.jar");
    eu.setName("firebird-1024");
    eu.setArguments("");
    eu.setMainClassName("");
    work.setExecutionUnit(eu);

    Worker worker = new Worker();
    worker.setId(9L);
    worker.setPort(PORT);
    worker.setAddress("localhost");

    final ThreadLocal<List<Worker>> workers = new ThreadLocal<List<Worker>>();
    workers.set(Arrays.asList(worker));


    when(mockWorker.doWork(any(com.sky.commons.Work.class))).thenReturn("test123123");
    when(workerRepository.findByState(eq(Worker.State.IDLE))).thenReturn(workers.get());
    when(workRepository.save(eq(work))).thenReturn(work);

    assertThat(workerService.doWork(work).get(), is(true));
    assertThat(worker.getWorks().get(0), is(work));

    verify(mockWorker).doWork(eq(workerService.toWork(work)));

    /* -------------------------------------------------------------------------------------------------- */
    reset(workRepository, workerRepository, mockWorker);

    Work work0 = new Work(), work1 = new Work();
    work0.setId(10L);
    eu = new ExecutionUnit();
    eu.setId(11L);
    eu.setJarFile(new byte[]{1,2,3,4});
    eu.setJarFileName("test0.jar");
    eu.setName("firebird-0");
    eu.setArguments("");
    eu.setMainClassName("");
    work0.setExecutionUnit(eu);

    work1.setId(11L);
    eu = new ExecutionUnit();
    eu.setId(12L);
    eu.setJarFile(new byte[]{5,6,7,8});
    eu.setJarFileName("test1.jar");
    eu.setName("firebird-1");
    eu.setArguments("");
    eu.setMainClassName("");
    work1.setExecutionUnit(eu);

    Worker worker0 = new Worker();
    worker0.setId(13L);
    worker0.setPort(PORT);
    worker0.setAddress("localhost");
    worker0.setState(Worker.State.IDLE);

    Worker worker1 = new Worker();
    worker1.setId(14L);
    worker1.setPort(PORT);
    worker1.setAddress("localhost");
    worker1.setState(Worker.State.IDLE);

    workers.set(Arrays.asList(worker0, worker1));

    when(mockWorker.doWork(any(com.sky.commons.Work.class))).thenReturn("test123123");
    doAnswer(new Answer<List<Worker>>() {
      @Override
      public List<Worker> answer(InvocationOnMock invocation) throws Throwable {
        Worker.State state = (Worker.State) invocation.getArguments()[0];

        for (Worker worker : workers.get()) {
          if (state.equals(worker.getState()))
            return Arrays.asList(worker);
        }

        return new LinkedList<Worker>();
      }
    }).when(workerRepository).findByState(eq(Worker.State.IDLE));
    doReturn(work0).when(workRepository).save(eq(work0));
    doReturn(work1).when(workRepository).save(eq(work1));

    assertThat(workerService.doWork(work0).get(), is(true));
    assertThat(worker0.getWorks().get(0), is(work0));
    assertThat(workerService.doWork(work1).get(), is(true));
    assertThat(worker1.getWorks().get(0), is(work1));

    verify(mockWorker).doWork(eq(workerService.toWork(work1)));
    verify(mockWorker).doWork(eq(workerService.toWork(work0)));
  }
}
