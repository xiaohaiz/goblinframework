package org.goblinframework.embedded.jetty.server

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.service.GoblinManagedStopWatch
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerId
import org.goblinframework.embedded.server.EmbeddedServerMXBean
import org.goblinframework.embedded.server.EmbeddedServerMode
import org.goblinframework.embedded.setting.ServerSetting
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("JettyEmbedded")
@GoblinManagedStopWatch
class JettyEmbeddedServer(private val setting: ServerSetting)
  : GoblinManagedObject(), EmbeddedServer, EmbeddedServerMXBean {

  private val server = AtomicReference<JettyEmbeddedServerImpl?>()

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
    logger.debug("{EMBEDDED} Embedded server [{}] started at [{}:{}]", id().asText(), getHost(), getPort())
  }

  override fun stop() {
    server.getAndSet(null)?.run {
      this.dispose()
      logger.debug("{EMBEDDED} Embedded server [{}] stopped", id().asText())
    }
  }

  override fun isRunning(): Boolean {
    return server.get() != null
  }

  override fun getUpTime(): String? {
    return stopWatch?.toString()
  }

  override fun getMode(): EmbeddedServerMode {
    return EmbeddedServerMode.JETTY
  }

  override fun getName(): String {
    return setting.name()
  }

  override fun getRunning(): Boolean {
    return isRunning()
  }

  override fun getHost(): String? {
    return server.get()?.host
  }

  override fun getPort(): Int? {
    return server.get()?.port
  }

  override fun dispose() {
    stop()
  }
}