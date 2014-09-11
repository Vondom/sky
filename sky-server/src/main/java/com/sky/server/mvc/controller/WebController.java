package com.sky.server.mvc.controller;

import com.sky.server.mvc.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

  @RequestMapping(value = "/test")
  public String test() {
    return "/test";
  }


}
