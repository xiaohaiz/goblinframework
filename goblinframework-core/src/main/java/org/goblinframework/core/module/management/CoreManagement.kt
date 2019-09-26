package org.goblinframework.core.module.management

import org.goblinframework.api.core.Singleton
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.serialization.SerializerManager
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
@RequestMapping("/goblin/core")
class CoreManagement private constructor() {

  companion object {
    @JvmField val INSTANCE = CoreManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("CompressorManagerMXBean", CompressorManager.INSTANCE)
    model.addAttribute("SerializerManagerMXBean", SerializerManager.INSTANCE)
    return "core/index"
  }
}