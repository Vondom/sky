package com.sky.server.mvc.controller.thrift;

import com.sky.server.config.annotation.TService;
import com.sky.server.exception.WebNotFoundException;
import org.apache.thrift.TException;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by jcooky on 2014. 7. 26..
 */
@Controller
public class TController {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(TController.class);

  private TProtocolFactory inFactory = new TBinaryProtocol.Factory(), outFactory = new TBinaryProtocol.Factory();

  private Collection<Map.Entry<String, String>> customHeaders;

  @Autowired
  private ApplicationContext applicationContext;
  private TMultiplexedProcessor processor = new TMultiplexedProcessor();

  @PostConstruct
  public final void init() throws ServletException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    this.customHeaders = new ArrayList<Map.Entry<String, String>>();

    if (inFactory == null) {
      throw new ServletException("inFactory must be set");
    }
    if (outFactory == null) {
      throw new ServletException("outFactory must be set");
    }

    for (Object bean : applicationContext.getBeansWithAnnotation(TService.class).values()) {
      TService tService = AnnotationUtils.findAnnotation(bean.getClass(), TService.class);
      Class<?> processorCls = null, ifaceCls = null;
      TProcessor processor = null;

      for (Class<?> cls : tService.thrift().getDeclaredClasses()) {
        if ("Processor".equals(cls.getSimpleName())) {
          processorCls = cls;
        } else if ("Iface".equals(cls.getSimpleName())) {
          ifaceCls = cls;
        }
      }

      if (processorCls != null && ifaceCls != null) {
        processor = (TProcessor) processorCls.getConstructor(ifaceCls).newInstance(bean);
        this.processor.registerProcessor(tService.name(), processor);
      }
    }
  }

  @RequestMapping(value = "/api/thrift", method = RequestMethod.POST)
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, WebNotFoundException {
    TTransport inTransport = null;
    TTransport outTransport = null;

    try {
      response.setContentType("application/x-thrift");

      if (null != this.customHeaders) {
        for (Map.Entry<String, String> header : this.customHeaders) {
          response.addHeader(header.getKey(), header.getValue());
        }
      }

      InputStream in = request.getInputStream();
      OutputStream out = response.getOutputStream();

      TTransport transport = new TIOStreamTransport(in, out);
      inTransport = transport;
      outTransport = transport;

      TProtocol inProtocol = inFactory.getProtocol(inTransport);
      TProtocol outProtocol = outFactory.getProtocol(outTransport);

      processor.process(inProtocol, outProtocol);

      out.flush();
    } catch (TException te) {
      throw new ServletException(te);
    }
  }

//  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//      throws ServletException, IOException {
//    doPost(req, resp);
//  }

  public void addCustomHeader(final String key, final String value) {
    this.customHeaders.add(new Map.Entry<String, String>() {
      public String getKey() {
        return key;
      }

      public String getValue() {
        return value;
      }

      public String setValue(String value) {
        return null;
      }
    });
  }

  public void setCustomHeaders(Collection<Map.Entry<String, String>> headers) {
    this.customHeaders.clear();
    this.customHeaders.addAll(headers);
  }

}
