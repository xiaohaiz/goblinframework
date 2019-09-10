package org.goblinframework.serialization.core.module.management

import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/serialization")
class SerializationManagementController private constructor() {

  companion object {
    @JvmField val INSTANCE = SerializationManagementController()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    return "serialization/index"
  }
}