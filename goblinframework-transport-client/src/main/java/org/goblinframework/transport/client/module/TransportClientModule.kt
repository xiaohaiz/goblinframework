package org.goblinframework.transport.client.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule

@Install
class TransportClientModule : GoblinChildModule {

  override fun name(): String {
    return "TRANSPORT:CLIENT"
  }
}