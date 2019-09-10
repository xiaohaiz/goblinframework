package org.goblinframework.embedded.jetty.provider

import org.goblinframework.embedded.core.EmbeddedServer
import org.goblinframework.embedded.core.setting.ServerSetting
import java.util.concurrent.atomic.AtomicReference

class JettyEmbeddedServer(private val setting: ServerSetting) : EmbeddedServer {

  private val server = AtomicReference<JettyEmbeddedServerImpl>()

  @Synchronized
  override fun start() {
    if (server.get() != null) {
      return
    }
    val running = JettyEmbeddedServerImpl(setting)
    server.set(running)
  }

  override fun stop() {
    server.getAndSet(null)?.run { close() }
  }

  override fun isRunning(): Boolean {
    return server.get() != null
  }
}