package org.goblinframework.management.controller

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleDefinition
import org.goblinframework.core.bootstrap.GoblinModuleLoader
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/")
class ManagementController private constructor() {

  companion object {
    @JvmField val INSTANCE = ManagementController()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    val modules = mutableListOf<GoblinModule>()
    for (name in GoblinModuleDefinition.moduleNames) {
      val module = GoblinModuleLoader.INSTANCE.getGoblinModule(name) ?: continue
      module.managementEntrance()?.run {
        modules.add(module)
      }
    }
    model.addAttribute("modules", modules)
    return "index"
  }
}