package org.goblinframework.transport.server.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.transport.server.channel.TransportServerManager

@Install
class TransportServerModule : GoblinChildModule {

  override fun name(): String {
    return "TRANSPORT:SERVER"
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    TransportServerManager.INSTANCE.close()
  }
}