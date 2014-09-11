package com.sky.server.mvc.service.thrift;

import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.repository.WorkerRepository;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class WorkManagerServiceTest extends SpringBasedTestSupport {

  @InjectMocks
  private WorkManagerService workManagerService;

  @Mock
  private WorkerRepository workerRepository;

  @Mock
  private WorkRepository workRepository;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
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
    assertEquals(id, (Long)workManagerService.add(address, port));
    verify(workerRepository).findByAddressAndPort(eq(address), eq(port));
    verify(workerRepository, times(1)).save(eq(verifyWorker));

    reset(workerRepository);

    // #2
    when(workerRepository.findByAddressAndPort(eq(address), eq(port))).thenReturn(worker);
    assertEquals(id, (Long)workManagerService.add(address, port));
    verify(workerRepository).findByAddressAndPort(eq(address), eq(port));
    verify(workerRepository, times(0)).save(any(Worker.class));

    reset(workerRepository);
  }

  @Test
  public void testDone() throws Exception {
    String address = "127.0.0.1";
    int port = 9200;
    Long id = 11L;

    Worker worker = new Worker();
    worker.setId(id);
    worker.setAddress(address);
    worker.setPort(port);

    doReturn(worker).when(workerRepository).findOne(eq(id));
    workManagerService.done(id);
    verify(workerRepository).findOne(eq(id));
    verify(workerRepository).delete(eq(worker));
  }

//  @Test
//  public void testTake() throws Exception {
//    String address = "127.0.0.1", name = "test", args = "test2";
//    int port = 9200;
//    long id = 11L, ordering = 2L;
//
//    Worker worker = new Worker();
//    worker.setId(id);
//    worker.setAddress(address);
//    worker.setPort(port);
//    worker.setState(Worker.State.IDLE);
//
//    Project project = new Project();
//    project.setId(id);
//    project.setName(name);
//    project.setArguments(args);
//    project.setJarFileName(address);
//    project.setJarFile(null);
//
//    Work work = new Work();
//    work.setId(id);
//    work.setOrdering(ordering);
//    work.setProject(project);
//
//    // #1
//    when(workerRepository.findByAddressAndPort(eq(address), eq(port))).thenReturn(null);
//    assertNull(workManagerService.take(address));
//    verify(workerRepository).findByName(address);
//
//    reset(workerRepository);
//
//    // #2
//    when(workerRepository.findByName(address)).thenReturn(worker);
//    when(workRepository.findWork()).thenReturn(null);
//    assertNull(workManagerService.take(address));
//    verify(workerRepository).findByName(address);
//    verify(workRepository).findWork();
//
//    reset(workerRepository, workRepository);
//
//    // #3
//    when(workerRepository.findByName(address)).thenReturn(worker);
//    when(workRepository.findWork()).thenReturn(work);
//    when(workRepository.save(work)).thenReturn(work);
//    assertEquals(new com.sky.commons.Work()
//        .setArguments(args)
//        .setProjectId(id)
//        .setJar(new Jar()
//            .setFile((byte[]) null)
//            .setName(address)), workManagerService.take(address));
//    verify(workerRepository).findByName(address);
//    verify(workRepository).findWork();
//    verify(workRepository).save(work);
//
//    reset(workerRepository, workRepository);
//  }

}