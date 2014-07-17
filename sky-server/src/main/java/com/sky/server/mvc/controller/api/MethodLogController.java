package com.sky.server.mvc.controller.api;

import com.sky.server.mvc.model.MethodLog;
import com.sky.server.mvc.repository.MethodLogRepository;
import com.sky.server.mvc.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by jcooky on 2014. 7. 13..
 */
@RestController
@RequestMapping("/api/methodLog")
public class MethodLogController {

  @Autowired
  private MethodLogRepository methodLogRepository;

  @Autowired
  private ProfileRepository profileRepository;

  @RequestMapping(value = "/byProfile/{id}")
  @Transactional(readOnly = true)
  public Collection<? extends MethodLog> getByProfile(@PathVariable Long id) {
    return profileRepository.findOne(id).getMethodLogs();
  }

  @RequestMapping(method = RequestMethod.PUT)
  @Transactional
  public void put(@RequestBody MethodLog methodLog) {
    methodLogRepository.save(methodLog);
  }

}
