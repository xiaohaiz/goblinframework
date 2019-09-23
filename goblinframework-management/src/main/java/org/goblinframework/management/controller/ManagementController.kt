package org.goblinframework.management.controller

import org.goblinframework.api.system.GoblinModule
import org.goblinframework.api.system.IModule
import org.goblinframework.core.system.ModuleLoader
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/")
class ManagementController private constructor() {

  companion object {
    @JvmField val INSTANCE = ManagementController()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    val modules = mutableListOf<IModule>()
    for (id in GoblinModule.values()) {
      val module = ModuleLoader.module(id) ?: continue
      module.managementEntrance()?.run { modules.add(module) }
    }
    model.addAttribute("modules", modules)
    return "index"
  }
}