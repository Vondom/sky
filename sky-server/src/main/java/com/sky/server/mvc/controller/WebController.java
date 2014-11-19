package com.sky.server.mvc.controller;

import com.sky.server.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index() {
    // TODO change view name for forwarding
    return "index";
  }

  @RequestMapping("/profile/{id}")
  public ModelAndView profile(@PathVariable long id) {
    return new ModelAndView("profile")
        .addObject("id", id);
  }


  @RequestMapping("/project/{userId}")
  public ModelAndView project(@PathVariable String userId) {
    return new ModelAndView("project")
        .addObject("userId", userId);
  }

  @RequestMapping("/project")
  public ModelAndView project() {
    return project(Long.toString(userService.getMe().getId()));
  }
}
