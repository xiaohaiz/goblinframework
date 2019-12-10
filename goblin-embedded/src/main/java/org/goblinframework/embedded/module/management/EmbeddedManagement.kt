package org.goblinframework.embedded.module.management

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.embedded.server.EmbeddedServerManager
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
@RequestMapping("/goblin/embedded")
class EmbeddedManagement private constructor() {

  companion object {
    @JvmField val INSTANCE = EmbeddedManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("embeddedServerManager", EmbeddedServerManager.INSTANCE)
    return "goblin/embedded/index"
  }
}