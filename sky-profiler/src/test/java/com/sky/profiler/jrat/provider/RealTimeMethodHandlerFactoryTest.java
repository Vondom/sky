//package com.sky.profiler.jrat.provider;
//
//import com.sky.commons.MethodProfile;
//import com.sky.profiler.thrift.AgentControlServiceQueue;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//import org.shiftone.jrat.core.MethodKey;
//import org.shiftone.jrat.core.spi.MethodHandler;
//import org.shiftone.jrat.core.spi.RuntimeContext;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.*;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.*;
//
//public class
//    RealTimeMethodHandlerFactoryTest {
//  @InjectMocks
//  private RealTimeMethodHandlerFactory factory;
//
//  @Mock
//  private AgentControlServiceQueue collector;
//
//  private List<MethodProfile> profiles = new ArrayList<MethodProfile>();
//
//  @Before
//  public void setUp() throws Exception {
//    factory = new RealTimeMethodHandlerFactory();
//    MockitoAnnotations.initMocks(this);
//
//    factory.setWorkId(11L);
//
//    doAnswer(new Answer() {
//      @Override
//      public Object answer(InvocationOnMock invocation) throws Throwable {
//        MethodProfile methodProfile = (MethodProfile) invocation.getArguments()[0];
//        profiles.add(methodProfile);
//
//        return null;
//      }
//    }).when(collector).put(any(MethodProfile.class));
//  }
//
//  @Test
//  public void testCreateMethodHandler() throws Exception {
//    MethodKey methodKey = MethodKey.getInstance("com.sky.test.Test", "run", "V();"),
//        methodKey2 = MethodKey.getInstance("com.sky.test.Test", "attach", "V();"),
//        methodKey1 = MethodKey.getInstance("com.sky.test.Test", "asdasd", "V();");
//
//    factory.startup(mock(RuntimeContext.class));
//    MethodHandler methodHandler = factory.createMethodHandler(methodKey);
//    MethodHandler methodHandler2 = factory.createMethodHandler(methodKey2);
//    MethodHandler methodHandler1 = factory.createMethodHandler(methodKey1);
//
//    methodHandler.onMethodStart();
//    methodHandler1.onMethodStart();
//    methodHandler1.onMethodFinish(15L, null);
//    methodHandler2.onMethodStart();
//    methodHandler2.onMethodFinish(10L, null);
//    methodHandler.onMethodFinish(100L, null);
//    factory.shutdown();
//
//    while(profiles.size() < 3) { Thread.sleep(1); }
//    MethodProfile profile2 = profiles.get(1), profile = profiles.get(2), profile1 = profiles.get(0);
//
//    verify(collector, times(3)).put(any(MethodProfile.class));
//
//    assertEquals(profile1.elapsedTime, 15L);
//    assertThat(profile1.callee.classKey.getClassName(), is(methodKey1.getClassName()));
//    assertThat(profile1.callee.classKey.getPackageName(), is(methodKey1.getPackageName()));
//    assertThat(profile1.callee.methodName, is(methodKey1.getMethodName()));
//    assertThat(profile1.callee.signature, is(methodKey1.getSignature()));
//    assertThat(profile1.caller.methodName, is(methodKey.getMethodName()));
//
//    assertEquals(profile2.getElapsedTime(), 10L);
////    assertEquals(profile2.getTotalElapsedTime(), 10L);
//    assertEquals(profile2.getCallee().getClassKey().getClassName(), methodKey2.getClassName());
//    assertEquals(profile2.getCallee().getClassKey().getPackageName(), methodKey2.getPackageName());
//    assertEquals(profile2.getCallee().getMethodName(), methodKey2.getMethodName());
//    assertEquals(profile2.getCallee().getSignature(), methodKey2.getSignature());
//    assertEquals(methodKey.getMethodName(), profile2.caller.getMethodName());
//
//    assertEquals(profile.getElapsedTime(), 100L);
////    assertEquals(profile.getTotalElapsedTime(), 100L);
//    assertEquals(profile.getCallee().getClassKey().getClassName(), methodKey.getClassName());
//    assertEquals(profile.getCallee().getClassKey().getPackageName(), methodKey.getPackageName());
//    assertEquals(profile.getCallee().getMethodName(), methodKey.getMethodName());
//    assertEquals(profile.getCallee().getSignature(), methodKey.getSignature());
//
//    assertTrue(profiles.get(0).getIndex() > profiles.get(1).getIndex());
//    assertTrue(profiles.get(1).getTimestamp() >= profiles.get(0).getTimestamp());
//  }
//}