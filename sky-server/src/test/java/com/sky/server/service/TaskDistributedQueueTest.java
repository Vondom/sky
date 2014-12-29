package com.sky.server.service;

import com.sky.server.test.SpringBasedTestSupport;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.junit.*;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class TaskDistributedQueueTest {
  private static final Logger logger = LoggerFactory.getLogger(TaskDistributedQueueTest.class);

  private static TestingServer zkTestServer;

  @InjectMocks
  private TaskDistributedQueue queue;

  private CuratorFramework curatorFramework;
  private SimpleDistributedQueue simpleDistributedQueue;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {

  }

  @Before
  public void setUp() throws Exception {

    zkTestServer = new TestingServer();
    curatorFramework = Mockito.spy(CuratorFrameworkFactory.newClient(zkTestServer.getConnectString(), new RetryOneTime(100)));
    curatorFramework.start();
    simpleDistributedQueue = Mockito.spy(new SimpleDistributedQueue(curatorFramework, "/tasks"));

    MockitoAnnotations.initMocks(this);
  }

  @After
  public void tearDown() throws IOException {
    curatorFramework.close();
    zkTestServer.close();
  }

  @Test
  public void testPut() throws Exception {
    queue.put(11L);

    List<String> children = curatorFramework.getChildren().forPath("/tasks");

    assertThat(children.size(), is(1));
    assertThat(children.get(0), startsWith("qn-000"));
  }

  @Test
  public void testTake() throws Exception {
    testPut();

    long id = queue.take();

    assertThat(id, is(11L));
  }
}