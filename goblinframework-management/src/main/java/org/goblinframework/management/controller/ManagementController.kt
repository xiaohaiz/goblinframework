package org.goblinframework.management.controller

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/")
class ManagementController private constructor() {

  companion object {
    @JvmField val INSTANCE = ManagementController()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    return "index"
  }
}