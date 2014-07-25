package com.sky.server.mvc.controller;

import com.sky.server.mvc.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jcooky on 2014. 7. 9..
 */
@Controller
public class WebController {

  private UserRepository userRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index() {
    return "/index";
  }

  @RequestMapping(value = "/methodLogs/{id}")
  public ModelAndView methodLogs(@PathVariable Integer id) {
    return new ModelAndView("method-logs")
        .addObject("profileId", id);
  }

  @RequestMapping(value = "/test")
  public String test() {
    return "/test";
  }


}
