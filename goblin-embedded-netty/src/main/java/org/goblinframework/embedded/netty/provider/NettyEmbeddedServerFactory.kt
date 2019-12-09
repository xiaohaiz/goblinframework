package org.goblinframework.embedded.netty.provider

import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerFactory
import org.goblinframework.embedded.server.EmbeddedServerMode

class NettyEmbeddedServerFactory : EmbeddedServerFactory {

  override fun mode(): EmbeddedServerMode {
    return EmbeddedServerMode.NETTY
  }

  override fun createEmbeddedServer(setting: ServerSetting): EmbeddedServer {
    return NettyEmbeddedServer(setting)
  }
}