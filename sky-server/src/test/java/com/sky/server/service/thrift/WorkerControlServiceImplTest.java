package com.sky.server.service.thrift;

import com.sky.commons.Jar;
import com.sky.server.mvc.model.ExecutionUnit;
import com.sky.server.mvc.model.Work;
import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.repository.WorkerRepository;
import com.sky.server.test.SpringBasedTestSupport;
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

import javax.servlet.ServletRequest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class WorkerControlServiceImplTest extends SpringBasedTestSupport {
  private static final int PORT = 53121;

  @InjectMocks
  private WorkerControlServiceImpl workerControlServiceImpl;

  @Mock
  private WorkerRepository workerRepository;

  @Mock
  private WorkRepository workRepository;

  @Mock
  private ServletRequest request;

  @Mock
  public com.sky.commons.Worker.Iface mockWorker;

  private TServer server;

  private com.sky.commons.Work toWork(Work work) {
    return new com.sky.commons.Work()
        .setId(work.getId())
        .setJar(new Jar()
            .setFile(work.getExecutionUnit().getJarFile())
            .setName(work.getExecutionUnit().getJarFileName()))
        .setArguments(work.getExecutionUnit().getArguments());
  }

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
  public void testAddWorker() throws Exception {
    String address = "127.0.0.1";
    int port = 9200;
    Long id = 11L;

    Worker verifyWorker = new Worker();
    verifyWorker.setAddress(address);
    verifyWorker.setPort(port);

    Worker worker = new Worker();
    worker.setId(id);
    worker.setAddress(address);
    worker.setPort(port);

    // #1
    when(workerRepository.findByAddressAndPort(eq(address), eq(port))).thenReturn(null);
    when(workerRepository.save(any(Worker.class))).thenReturn(worker);
    assertEquals(id, (Long) workerControlServiceImpl.add(address, port));
    verify(workerRepository).findByAddressAndPort(eq(address), eq(port));
    verify(workerRepository, times(1)).save(eq(verifyWorker));

    reset(workerRepository);

    // #2
    when(workerRepository.findByAddressAndPort(eq(address), eq(port))).thenReturn(worker);
    when(workerRepository.save(any(Worker.class))).thenReturn(worker);
    assertEquals(id, (Long) workerControlServiceImpl.add(address, port));
    verify(workerRepository).findByAddressAndPort(eq(address), eq(port));
    verify(workerRepository, times(1)).save(eq(worker));

    reset(workerRepository);

    // #3
    when(request.getRemoteAddr()).thenReturn(address);
    when(workerRepository.findByAddressAndPort(eq(address), eq(port))).thenReturn(null);
    when(workerRepository.save(any(Worker.class))).thenReturn(worker);
    assertEquals(id, (Long) workerControlServiceImpl.add("0.0.0.0", port));
    verify(request).getRemoteAddr();
    verify(workerRepository).findByAddressAndPort(eq(address), eq(port));
    verify(workerRepository, times(1)).save(eq(verifyWorker));
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

    assertThat(workerControlServiceImpl.doWork(work).get(), is(true));
    assertThat(worker.getWorks().get(0), is(work));

    verify(mockWorker).doWork(eq(toWork(work)));

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

    assertThat(workerControlServiceImpl.doWork(work0).get(), is(true));
    assertThat(worker0.getWorks().get(0), is(work0));
    assertThat(workerControlServiceImpl.doWork(work1).get(), is(true));
    assertThat(worker1.getWorks().get(0), is(work1));

    verify(mockWorker).doWork(eq(toWork(work1)));
    verify(mockWorker).doWork(eq(toWork(work0)));
  }

}