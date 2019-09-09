package org.goblinframework.management.server

import org.goblinframework.core.module.spi.ManagementServer
import org.goblinframework.embedded.core.EmbeddedServerMode
import org.goblinframework.embedded.core.manager.EmbeddedServerManager
import org.goblinframework.embedded.core.setting.ServerSetting
import java.util.concurrent.atomic.AtomicReference

class ManagementServerManager private constructor() : ManagementServer {

  companion object {
    private const val SERVER_NAME = "GoblinManagementServer"
    @JvmField val INSTANCE = ManagementServerManager()
  }

  private val setting = AtomicReference<ServerSetting>()

  override fun start() {
    if (this.setting.get() != null) {
      return
    }
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
    this.setting.set(setting)
  }

  override fun stop() {
    setting.getAndSet(null)?.run {
      val serverManager = EmbeddedServerManager.INSTANCE
      serverManager.closeServer(this.name())
    }
  }

  class Installer : ManagementServer by INSTANCE
}