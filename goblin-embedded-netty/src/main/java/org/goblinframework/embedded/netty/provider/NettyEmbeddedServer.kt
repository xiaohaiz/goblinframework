package org.goblinframework.embedded.netty.provider

import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import java.util.concurrent.atomic.AtomicReference

class NettyEmbeddedServer(private val setting: ServerSetting) : EmbeddedServer {

  private val server = AtomicReference<NettyEmbeddedServerImpl>()

  @Synchronized
  override fun start() {
    if (server.get() != null) {
      return
    }
    val running = NettyEmbeddedServerImpl(setting)
    server.set(running)
  }

  override fun stop() {
    server.getAndSet(null)?.run { close() }
  }

  override fun isRunning(): Boolean {
    return server.get() != null
  }
}