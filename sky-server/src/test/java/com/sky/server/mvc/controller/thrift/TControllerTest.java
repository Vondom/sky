package com.sky.server.mvc.controller.thrift;

import com.sky.commons.RealtimeMethodProfileCollector;
import com.sky.server.test.SpringBasedTestSupport;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@Controller
@RequestMapping("/test/thrift")
class ThriftTestController extends TController {

  public RealtimeMethodProfileCollector.Iface mockService = mock(RealtimeMethodProfileCollector.Iface.class);

  @Override
  protected TProcessor getProcessor() {
    return new RealtimeMethodProfileCollector.Processor<RealtimeMethodProfileCollector.Iface>(mockService);
  }

  @Override
  protected TProtocolFactory getInProtocolFactory() {
    return new TCompactProtocol.Factory();
  }

  @Override
  protected TProtocolFactory getOutProtocolFactory() {
    return new TCompactProtocol.Factory();
  }
}

//@IntegrationTest({"server.port=0", "management.port=0"})
public class TControllerTest extends SpringBasedTestSupport {

  @Autowired
  private ThriftTestController controller;

  @Value("${local.server.port}")
  int serverPort;

  @Test
  public void testDoPost() throws Exception {
    when(controller.mockService.createProfile()).thenReturn(11L);

    RealtimeMethodProfileCollector.Iface collector = new RealtimeMethodProfileCollector.Client(new TCompactProtocol(new THttpClient("http://localhost:"+serverPort+"/test/thrift")));

    long result = collector.createProfile();

    assertEquals(11L, result);
    verify(controller.mockService).createProfile();
  }
}