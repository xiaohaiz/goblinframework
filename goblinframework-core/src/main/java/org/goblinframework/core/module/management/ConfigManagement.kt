package org.goblinframework.core.module.management

import org.goblinframework.api.common.Singleton
import org.goblinframework.core.config.ConfigLoader
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
    model.addAttribute("configLoaderMXBean", ConfigLoader.INSTANCE)
    return "config/index"
  }
}