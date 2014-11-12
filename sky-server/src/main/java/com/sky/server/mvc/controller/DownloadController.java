package com.sky.server.mvc.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.io.IOException;

/**
 * Created by jcooky on 2014. 6. 25..
 */
@RestController
@RequestMapping("/download")
public class DownloadController {
  @Autowired
  private ServletContext servletContext;

  @Autowired
  private ApplicationContext applicationContext;

  @RequestMapping(value = "/config/{workId}", produces = "text/xml")
  public String downloadConfig(@PathVariable long workId) throws IOException {

    Resource resource = applicationContext.getResource("classpath:jrat.xml");
    String str = IOUtils.toString(resource.getInputStream());
    return StringUtils.replaceEach(str, new String [] {"${workId}"},
        new String[] {Long.toString(workId)});
  }
}
