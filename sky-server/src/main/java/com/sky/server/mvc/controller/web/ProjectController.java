package com.sky.server.mvc.controller.web;

import com.sky.server.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jcooky on 2014. 7. 24..
 */
@Controller("webProjectController")
@RequestMapping(value = "/project", method = RequestMethod.GET)
public class ProjectController {
  @Autowired
  private UserService userService;

  @RequestMapping("/{userId}")
  public ModelAndView all(@PathVariable String userId) {
    return new ModelAndView("project/list")
        .addObject("userId", userId);
  }

  @RequestMapping("")
  public ModelAndView all() {
    return all(Long.toString(userService.getMe().getId()));
  }

  @RequestMapping("/create")
  public ModelAndView create() {
    return new ModelAndView("project/create")
        .addObject(userService.getMe().getId());
  }

}
