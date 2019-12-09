package org.goblinframework.embedded.jetty.server

import org.goblinframework.api.function.Disposable
import org.goblinframework.embedded.core.setting.ServerSetting

class JettyEmbeddedServerImpl(private val setting: ServerSetting) : Disposable {

  override fun dispose() {
  }
}