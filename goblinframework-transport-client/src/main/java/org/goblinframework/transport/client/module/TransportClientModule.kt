package org.goblinframework.transport.client.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.transport.client.handler.TransportClientManager

@Install
class TransportClientModule : GoblinChildModule {

  override fun name(): String {
    return "TRANSPORT:CLIENT"
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    TransportClientManager.INSTANCE.close()
  }
}