package org.goblinframework.embedded.jetty.server

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerFactory
import org.goblinframework.embedded.server.EmbeddedServerMode

@Singleton
class JettyEmbeddedServerFactory private constructor() : EmbeddedServerFactory {

  companion object {
    @JvmField val INSTANCE = JettyEmbeddedServerFactory()
  }

  override fun mode(): EmbeddedServerMode {
    return EmbeddedServerMode.JETTY
  }

  override fun createEmbeddedServer(setting: ServerSetting): EmbeddedServer {
    if (setting.mode() !== mode()) {
      throw UnsupportedOperationException()
    }
    return JettyEmbeddedServer(setting)
  }
}