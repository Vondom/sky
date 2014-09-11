package com.sky.server.mvc.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jcooky on 2014. 9. 11..
 */
@Controller("webWorkController")
@RequestMapping(value = "/work", method = RequestMethod.GET)
public class WorkController {

  @RequestMapping("/profile/{workId}")
  public ModelAndView pageProfile(@PathVariable long workId) {
    return new ModelAndView("profile")
        .addObject("profileId", 11L)
        .addObject("workId", workId);
  }
}
