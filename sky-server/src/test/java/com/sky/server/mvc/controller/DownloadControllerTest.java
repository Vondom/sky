package com.sky.server.mvc.controller;

import com.sky.server.test.SpringBasedTestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DownloadControllerTest extends SpringBasedTestSupport {

  @Autowired
  private DownloadController controller;

  @Test
  public void testDownloadConfig() throws Exception {
    String xmlStr = MockMvcBuilders.standaloneSetup(controller)
        .build().perform(MockMvcRequestBuilders
          .get("/download/config/4/1")
          .accept(MediaType.TEXT_XML)).andReturn().getResponse().getContentAsString();

    assertThat(xmlStr, not(""));

    InputSource is = new InputSource(new StringReader(xmlStr));
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

    XPath xpath = XPathFactory.newInstance().newXPath();
    String str = (String) xpath.evaluate("//handler/property[@name='projectId']/@value", document, XPathConstants.STRING);

    assertThat(str, is("4"));
  }
}