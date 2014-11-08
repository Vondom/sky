package com.sky.profiler.jrat.provider;

import com.sky.commons.AgentControlService;
import com.sky.commons.MethodProfile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.shiftone.jrat.core.MethodKey;
import org.shiftone.jrat.core.spi.MethodHandler;
import org.shiftone.jrat.core.spi.RuntimeContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RealTimeMethodHandlerFactoryTest {
  @InjectMocks
  private RealTimeMethodHandlerFactory factory;

  @Mock
  private AgentControlService.Iface collector;

  private List<MethodProfile> profiles = new ArrayList<MethodProfile>();

  @Before
  public void setUp() throws Exception {
    factory = new RealTimeMethodHandlerFactory();
    MockitoAnnotations.initMocks(this);

    factory.setWorkId(11L);

    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        MethodProfile methodProfile = (MethodProfile) invocation.getArguments()[0];
        profiles.add(methodProfile);

        return null;
      }
    }).when(collector).put(any(MethodProfile.class));
  }

  @Test
  public void testCreateMethodHandler() throws Exception {
    MethodKey methodKey = MethodKey.getInstance("com.sky.test.Test", "run", "V();"),
        subMethodKey = MethodKey.getInstance("com.sky.test.Test", "attach", "V();");

    factory.startup(mock(RuntimeContext.class));
    MethodHandler methodHandler = factory.createMethodHandler(methodKey);
    MethodHandler methodHandler2 = factory.createMethodHandler(subMethodKey);

    methodHandler.onMethodStart();
    methodHandler2.onMethodStart();
    methodHandler2.onMethodFinish(10L, null);
    methodHandler.onMethodFinish(100L, null);
    factory.shutdown();

    verify(collector, times(2)).put(any(MethodProfile.class));
    assertEquals(profiles.get(0).getElapsedTime(), 10L);
    assertEquals(profiles.get(0).getTotalElapsedTime(), 10L);
    assertEquals(profiles.get(0).getCallee().getClassKey().getClassName(), subMethodKey.getClassName());
    assertEquals(profiles.get(0).getCallee().getClassKey().getPackageName(), subMethodKey.getPackageName());
    assertEquals(profiles.get(0).getCallee().getMethodName(), subMethodKey.getMethodName());
    assertEquals(profiles.get(0).getCallee().getSignature(), subMethodKey.getSignature());

    assertEquals(profiles.get(1).getElapsedTime(), 90L);
    assertEquals(profiles.get(1).getTotalElapsedTime(), 100L);
    assertEquals(profiles.get(1).getCallee().getClassKey().getClassName(), methodKey.getClassName());
    assertEquals(profiles.get(1).getCallee().getClassKey().getPackageName(), methodKey.getPackageName());
    assertEquals(profiles.get(1).getCallee().getMethodName(), methodKey.getMethodName());
    assertEquals(profiles.get(1).getCallee().getSignature(), methodKey.getSignature());

    assertTrue(profiles.get(0).getIndex() > profiles.get(1).getIndex());
    assertTrue(profiles.get(1).getTimestamp() >= profiles.get(0).getTimestamp());
  }
}