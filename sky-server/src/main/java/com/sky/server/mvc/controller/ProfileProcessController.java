package com.sky.server.mvc.controller;

import com.sky.commons.RealtimeMethodProfile;
import com.sky.server.mvc.model.Profile;
import com.sky.server.mvc.service.ProfileProcessService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by jcooky on 2014. 7. 7..
 */
@RestController
@RequestMapping("/agent/profile")
public class ProfileProcessController {

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ProfileProcessService profileProcessService;

  @RequestMapping(method = RequestMethod.POST)
  public long create() {
    logger.info("CALL {}.create()", getClass().getName());

    return profileProcessService.create(new Profile());
  }

  @RequestMapping(method = RequestMethod.PUT)
  public void put(InputStream is) throws IOException, ClassNotFoundException {
    ObjectInputStream ois = null;
    RealtimeMethodProfile realtimeMethodProfile = null;
    try {
      ois = new ObjectInputStream(is);
      realtimeMethodProfile = (RealtimeMethodProfile)ois.readObject();
    } finally {
      if (ois != null) ois.close();
    }

    logger.debug("realtimeMethodProfile: {}", realtimeMethodProfile);

    profileProcessService.put(realtimeMethodProfile);
  }
}
