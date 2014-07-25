package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.MethodLog;
import com.sky.server.mvc.repository.MethodLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jcooky on 2014. 7. 13..
 */
@RestController
@RequestMapping("/api/methodLog")
public class MethodLogController {

  @Autowired
  private MethodLogRepository methodLogRepository;

  @RequestMapping(method = RequestMethod.PUT)
  @Transactional
  public void put(@RequestBody MethodLog methodLog) {
    methodLogRepository.save(methodLog);
  }

}
