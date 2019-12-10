package org.goblinframework.embedded.jetty.server

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerId
import org.goblinframework.embedded.server.EmbeddedServerMXBean
import org.goblinframework.embedded.server.EmbeddedServerMode
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("JettyEmbedded")
class JettyEmbeddedServer(private val setting: ServerSetting)
  : GoblinManagedObject(), EmbeddedServer, EmbeddedServerMXBean {

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

  override fun dispose() {
    stop()
  }
}