package com.sky.server.mvc.controller.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TIOStreamTransport;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by jcooky on 2014. 7. 26..
 */
public abstract class TController implements InitializingBean {
  private static final long serialVersionUID = 1L;

  private TProcessor processor;

  private TProtocolFactory inFactory;

  private TProtocolFactory outFactory;

  private Collection<Map.Entry<String, String>> customHeaders;

  protected abstract TProcessor getProcessor();

  protected abstract TProtocolFactory getInProtocolFactory();

  protected abstract TProtocolFactory getOutProtocolFactory();

  public final void afterPropertiesSet() throws ServletException {
    this.processor = getProcessor();
    this.inFactory = getInProtocolFactory();
    this.outFactory = getOutProtocolFactory();
    this.customHeaders = new ArrayList<Map.Entry<String, String>>();

    if (processor == null) {
      throw new ServletException("processor must be set");
    }
    if (inFactory == null) {
      throw new ServletException("inFactory must be set");
    }
    if (outFactory == null) {
      throw new ServletException("outFactory must be set");
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
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
      TProtocol outProtocol = inFactory.getProtocol(outTransport);

      processor.process(inProtocol, outProtocol);
      out.flush();
    } catch (TException te) {
      throw new ServletException(te);
    }
  }
//
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
