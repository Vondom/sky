package com.sky.server.web;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by jcooky on 2014. 6. 25..
 */
@RestController
@RequestMapping("/download")
public class DownloadController {
  @Resource
  private ApplicationContext applicationContext;
//  @Autowired
//  private ServletContext servletContext;

    
  @RequestMapping(value = "/config/{workId}", produces = "text/xml")
  public String downloadConfig(@PathVariable long workId) throws IOException {

    String str = IOUtils.toString(applicationContext.getResource("classpath:jrat.xml").getInputStream());
    return StringUtils.replaceEach(str, new String [] {"${workId}"},
        new String[] {Long.toString(workId)});
  }
}
