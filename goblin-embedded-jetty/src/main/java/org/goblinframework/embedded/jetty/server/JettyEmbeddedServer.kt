package org.goblinframework.embedded.jetty.server

import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerId
import org.goblinframework.embedded.server.EmbeddedServerMode
import java.util.concurrent.atomic.AtomicReference

class JettyEmbeddedServer(private val setting: ServerSetting) : EmbeddedServer {

  private val server = AtomicReference<JettyEmbeddedServerImpl>()

  override fun id(): EmbeddedServerId {
    return EmbeddedServerId(EmbeddedServerMode.JETTY, setting.name())
  }

  @Synchronized
  override fun start() {
    if (server.get() != null) {
      return
    }
    val running = JettyEmbeddedServerImpl(setting)
    server.set(running)
  }

  override fun stop() {
    server.getAndSet(null)?.dispose()
  }

  override fun isRunning(): Boolean {
    return server.get() != null
  }
}