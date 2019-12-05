package org.goblinframework.example.webmvc.controller.core

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class IndexController {

  @RequestMapping("index.goblin")
  fun index(model: Model): String {
    return "index"
  }
}