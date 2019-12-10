package org.goblinframework.embedded.netty.server

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerFactory
import org.goblinframework.embedded.server.EmbeddedServerMode
import org.goblinframework.embedded.setting.ServerSetting

@Singleton
class NettyEmbeddedServerFactory private constructor() : EmbeddedServerFactory {

  companion object {
    @JvmField val INSTANCE = NettyEmbeddedServerFactory()
  }

  override fun mode(): EmbeddedServerMode {
    return EmbeddedServerMode.NETTY
  }

  override fun createEmbeddedServer(setting: ServerSetting): EmbeddedServer {
    if (setting.mode() !== mode()) {
      throw UnsupportedOperationException()
    }
    return NettyEmbeddedServer(setting)
  }
}