package org.goblinframework.transport.server.module

import org.goblinframework.api.core.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.transport.server.channel.TransportServerManager

@Install
class TransportServerModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.TRANSPORT_SERVER
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    TransportServerManager.INSTANCE.dispose()
  }
}