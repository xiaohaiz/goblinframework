package org.goblinframework.webmvc.module.management

import org.goblinframework.webmvc.handler.RequestHandlerManagerBuilder
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/webmvc")
class WebmvcManagementController private constructor() {

  companion object {
    @JvmField val INSTANCE = WebmvcManagementController()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("RequestHandlerManagerBuilderMXBean", RequestHandlerManagerBuilder.INSTANCE)
    return "webmvc/index"
  }
}