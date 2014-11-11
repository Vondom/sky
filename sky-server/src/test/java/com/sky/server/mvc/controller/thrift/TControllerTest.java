package com.sky.server.mvc.controller.thrift;

import com.sky.commons.AgentControlService;
import com.sky.commons.MethodProfile;
import com.sky.server.config.annotation.TService;
import com.sky.server.test.SpringBasedTestSupport;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.THttpClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@TService(name = "test", thrift = AgentControlService.class)
class ThriftTestService implements AgentControlService.Iface {

  public static AgentControlService.Iface mockService;

  @Override
  public void put(MethodProfile methodProfile) throws TException {
    mockService.put(methodProfile);
  }

  @PostConstruct
  public void init() {
    mockService = mock(AgentControlService.Iface.class);
  }
}

//@IntegrationTest({"server.port=0", "management.port=0"})
public class TControllerTest extends SpringBasedTestSupport {

  @Autowired
  private ThriftTestService service;

  @Value("${local.server.port}")
  int serverPort;

  @Test
  public void testDoPost() throws Exception {
    long id = 10L;

    assertNotNull(ThriftTestService.mockService);
//    doReturn(11L).when(ThriftTestService.mockService).put(any(MethodProfile.class));

    AgentControlService.Iface collector = new AgentControlService.Client(new TCompactProtocol(new THttpClient("http://localhost:"+serverPort+"/agent/test")));

    collector.put(new MethodProfile());

//    assertEquals(11L, result);
    verify(ThriftTestService.mockService).put(any(MethodProfile.class));
  }
}