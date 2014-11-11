package com.sky.profiler.thrift;

import com.sky.commons.AgentControlService;
import com.sky.commons.MethodProfile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

public class AgentControlServiceQueueTest {
  @InjectMocks
  private AgentControlServiceQueue serviceQueue;

  @Mock
  private AgentControlService.Iface iface;

  private List<MethodProfile> profiles = new ArrayList<MethodProfile>();

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        MethodProfile methodProfile = (MethodProfile) invocation.getArguments()[0];
        profiles.add(methodProfile);

        return null;
      }
    }).when(iface).put(any(MethodProfile.class));
  }

  @Test
  public void testPut() throws Exception {
    MethodProfile methodProfile = new MethodProfile();
    methodProfile.workId = 11L;

    serviceQueue.start();
    serviceQueue.put(methodProfile);
    Thread.sleep(1000);
    serviceQueue.finish();

    assertThat(profiles.get(0), is(methodProfile));

    verify(iface).put(eq(methodProfile));
  }
}