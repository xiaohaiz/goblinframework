package org.goblinframework.embedded.jetty.provider

import org.goblinframework.embedded.core.EmbeddedServer
import org.goblinframework.embedded.core.EmbeddedServerMode
import org.goblinframework.embedded.core.module.spi.EmbeddedServerFactory
import org.goblinframework.embedded.core.setting.ServerSetting

class JettyEmbeddedServerFactory : EmbeddedServerFactory {

  override fun mode(): EmbeddedServerMode {
    return EmbeddedServerMode.JETTY
  }

  override fun createEmbeddedServer(setting: ServerSetting): EmbeddedServer {
    return JettyEmbeddedServer(setting)
  }
}