package com.sky.server.mvc.service.thrift;

import com.sky.server.mvc.model.Worker;
import com.sky.server.mvc.repository.WorkRepository;
import com.sky.server.mvc.service.WorkerService;
import com.sky.server.test.SpringBasedTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class WorkerControlServiceImplTest extends SpringBasedTestSupport {


  @InjectMocks
  private WorkerControlServiceImpl workerControlServiceImpl;

  @Mock
  private WorkerService workerService;

  @Mock
  private WorkRepository workRepository;

  @Mock
  private ServletRequest request;

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
    when(workerService.get(eq(address), eq(port))).thenReturn(null);
    when(workerService.save(any(Worker.class))).thenReturn(worker);
    assertEquals(id, (Long) workerControlServiceImpl.add(address, port));
    verify(workerService).get(eq(address), eq(port));
    verify(workerService, times(1)).save(eq(verifyWorker));

    reset(workerService);

    // #2
    when(workerService.get(eq(address), eq(port))).thenReturn(worker);
    when(workerService.save(any(Worker.class))).thenReturn(worker);
    assertEquals(id, (Long) workerControlServiceImpl.add(address, port));
    verify(workerService).get(eq(address), eq(port));
    verify(workerService, times(1)).save(eq(worker));

    reset(workerService);

    // #3
    when(request.getRemoteAddr()).thenReturn(address);
    when(workerService.get(eq(address), eq(port))).thenReturn(null);
    when(workerService.save(any(Worker.class))).thenReturn(worker);
    assertEquals(id, (Long) workerControlServiceImpl.add("0.0.0.0", port));
    verify(request).getRemoteAddr();
    verify(workerService).get(eq(address), eq(port));
    verify(workerService, times(1)).save(eq(verifyWorker));
  }

}