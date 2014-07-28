package com.sky.server.mvc.controller.thrift.impl;

import com.sky.commons.RealtimeMethodProfileCollector;
import com.sky.server.mvc.controller.thrift.TController;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jcooky on 2014. 7. 26..
 */
@Controller
@RequestMapping("/agent/profile")
public class RealtimeMethodProfileCollectorController extends TController {

  @Autowired
  private RealtimeMethodProfileCollector.Iface realtimeMethodProfileCollectorService;

  @Override
  protected TProcessor getProcessor() {
    return new RealtimeMethodProfileCollector.Processor<RealtimeMethodProfileCollector.Iface>(realtimeMethodProfileCollectorService);
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
