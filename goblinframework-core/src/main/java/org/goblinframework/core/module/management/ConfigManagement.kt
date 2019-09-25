package org.goblinframework.core.module.management

import org.goblinframework.api.common.Singleton
import org.goblinframework.core.config.ConfigManager
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
@RequestMapping("/goblin/config")
class ConfigManagement private constructor() {

  companion object {
    @JvmField val INSTANCE = ConfigManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("configManagerMXBean", ConfigManager.INSTANCE)
    return "config/index"
  }
}