package org.goblinframework.remote.server.module.management

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.remote.server.handler.RemoteServerManager
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Singleton
@RequestMapping("/goblin/remote/server")
class RemoteServerManagement private constructor() {

  companion object {
    @JvmField val INSTANCE = RemoteServerManagement()
  }

  @RequestMapping("index.do")
  fun index(model: Model): String {
    model.addAttribute("remoteServerManagerMXBean", RemoteServerManager.INSTANCE)
    return "remote/server/index"
  }
}