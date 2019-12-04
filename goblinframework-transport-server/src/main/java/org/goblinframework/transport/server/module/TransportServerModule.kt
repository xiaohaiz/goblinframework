package org.goblinframework.transport.server.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.transport.server.channel.TransportServerManager
import org.goblinframework.transport.server.module.management.TransportServerManagement

@Install
class TransportServerModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.TRANSPORT_SERVER
  }

  override fun managementEntrance(): String? {
    return if (TransportServerManager.INSTANCE.getTransportServerList().isNotEmpty()) {
      "/goblin/transport/server/index.do"
    } else null
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerManagementController(TransportServerManagement.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    TransportServerManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    TransportServerManager.INSTANCE.dispose()
  }
}