package org.goblinframework.embedded.jetty.server

import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerFactory
import org.goblinframework.embedded.server.EmbeddedServerMode

class JettyEmbeddedServerFactory : EmbeddedServerFactory {

  override fun mode(): EmbeddedServerMode {
    return EmbeddedServerMode.JETTY
  }

  override fun createEmbeddedServer(setting: ServerSetting): EmbeddedServer {
    return JettyEmbeddedServer(setting)
  }
}