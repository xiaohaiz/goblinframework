package org.goblinframework.management.server

import org.goblinframework.core.module.spi.ManagementServer
import org.goblinframework.embedded.core.EmbeddedServerMode
import org.goblinframework.embedded.core.manager.EmbeddedServerManager
import org.goblinframework.embedded.core.setting.ServerSetting

class ManagementServerManager private constructor() : ManagementServer {

  companion object {
    private const val SERVER_NAME = "GoblinManagementServer"
    @JvmField val INSTANCE = ManagementServerManager()
  }

  override fun start() {
    val setting = ServerSetting.builder()
        .name(SERVER_NAME)
        .mode(EmbeddedServerMode.JDK)
        .applyHandlerSetting {
          it.contextPath("/")
          it.servletHandler { target, request, response ->
            println("")
          }
        }
        .build()
    val serverManager = EmbeddedServerManager.INSTANCE
    serverManager.createServer(setting).start()
  }

  override fun stop() {
    val serverManager = EmbeddedServerManager.INSTANCE
    serverManager.closeServer(SERVER_NAME)
  }

  class Installer : ManagementServer by INSTANCE
}