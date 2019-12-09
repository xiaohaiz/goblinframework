package org.goblinframework.embedded.java

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.embedded.core.setting.ServerSetting
import org.goblinframework.embedded.server.EmbeddedServer
import org.goblinframework.embedded.server.EmbeddedServerFactory
import org.goblinframework.embedded.server.EmbeddedServerMode

@Singleton
class JdkEmbeddedServerFactory private constructor() : EmbeddedServerFactory {

  companion object {
    @JvmField val INSTANCE = JdkEmbeddedServerFactory()
  }

  override fun mode(): EmbeddedServerMode {
    return EmbeddedServerMode.JAVA
  }

  override fun createEmbeddedServer(setting: ServerSetting): EmbeddedServer {
    return JdkEmbeddedServer(setting)
  }
}