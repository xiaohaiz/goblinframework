package org.goblinframework.remote.server.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInitializeContext
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import org.goblinframework.remote.server.transport.RemoteTransportServerManager

@Install
class RemoteServerModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_SERVER
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteServerConfigManager.INSTANCE.initialize()
    RemoteTransportServerManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RemoteTransportServerManager.INSTANCE.dispose()
    RemoteServerConfigManager.INSTANCE.dispose()
  }
}