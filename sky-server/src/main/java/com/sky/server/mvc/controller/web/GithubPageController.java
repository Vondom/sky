package com.sky.server.mvc.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jcooky on 2014. 10. 6..
 */
@Controller
@RequestMapping("/github")
public class GithubPageController {

  @RequestMapping("/project")
  public String github() {
    return "github/project";
  }
}
