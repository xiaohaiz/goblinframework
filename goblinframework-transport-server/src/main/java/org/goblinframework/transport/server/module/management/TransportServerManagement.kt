package org.goblinframework.transport.server.module.management

import org.goblinframework.transport.server.channel.TransportServerManager
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/goblin/transport/server")
class TransportServerManagement private constructor() {

  companion object {
    val INSTANCE = TransportServerManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("transportServerManager", TransportServerManager.INSTANCE)
    return "goblin/transport/server/index"
  }
}