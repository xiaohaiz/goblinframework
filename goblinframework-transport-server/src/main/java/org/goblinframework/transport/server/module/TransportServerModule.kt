package org.goblinframework.transport.server.module

import org.goblinframework.core.bootstrap.GoblinChildModule

class TransportServerModule : GoblinChildModule {

  override fun name(): String {
    return "TRANSPORT:SERVER"
  }
}