package org.goblinframework.embedded.netty.provider

import org.goblinframework.embedded.core.EmbeddedServer
import org.goblinframework.embedded.core.EmbeddedServerMode
import org.goblinframework.embedded.core.module.spi.EmbeddedServerFactory
import org.goblinframework.embedded.core.setting.ServerSetting

class NettyEmbeddedServerFactory : EmbeddedServerFactory {

  override fun mode(): EmbeddedServerMode {
    return EmbeddedServerMode.NETTY
  }

  override fun createEmbeddedServer(setting: ServerSetting): EmbeddedServer {
    return NettyEmbeddedServer(setting)
  }
}