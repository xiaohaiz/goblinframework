package org.goblinframework.management.controller

import org.goblinframework.api.core.Singleton
import org.goblinframework.core.system.*
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
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
    val subModules = mutableListOf<ISubModule>()
    for (id in GoblinSubModule.values()) {
      val subModule = SubModuleLoader.subModule(id) ?: continue
      subModule.managementEntrance()?.run { subModules.add(subModule) }
    }

    model.addAttribute("modules", modules)
    model.addAttribute("subModules", subModules)
    model.addAttribute("extModules", ExtModuleLoader.asList())
    return "index"
  }
}