package org.goblinframework.embedded.netty.server

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.service.GoblinManagedStopWatch
import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerId
import org.goblinframework.embedded.server.EmbeddedServerMXBean
import org.goblinframework.embedded.server.EmbeddedServerMode
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("NettyEmbedded")
@GoblinManagedStopWatch
class NettyEmbeddedServer(private val setting: ServerSetting)
  : GoblinManagedObject(), EmbeddedServer, EmbeddedServerMXBean {

  private val server = AtomicReference<NettyEmbeddedServerImpl>()

  override fun id(): EmbeddedServerId {
    return EmbeddedServerId(EmbeddedServerMode.NETTY, setting.name())
  }

  @Synchronized
  override fun start() {
    if (server.get() != null) {
      return
    }
    val running = NettyEmbeddedServerImpl(setting)
    server.set(running)
    logger.debug("{EMBEDDED} Embedded server [{}] started at [{}:{}]",
        id().asText(), server.get()!!.host, server.get()!!.port)
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

  override fun disposeBean() {
    stop()
  }
}