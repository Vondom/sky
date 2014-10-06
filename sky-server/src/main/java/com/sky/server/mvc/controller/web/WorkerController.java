package com.sky.server.mvc.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by jcooky on 2014. 9. 11..
 */
@Controller("webWorkerController")
@RequestMapping(value = "/worker", method = RequestMethod.GET)
public class WorkerController {

  @RequestMapping
  public ModelAndView page() {
    return new ModelAndView("worker/list");
  }
}
