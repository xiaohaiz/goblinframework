package org.goblinframework.remote.server.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.remote.server.dispatcher.response.RemoteServerResponseDispatcher
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import org.goblinframework.remote.server.service.RemoteServiceManager
import org.goblinframework.remote.server.transport.RemoteTransportServerManager

@Install
class RemoteServerModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_SERVER
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerPriorFinalizationTask { RemoteServiceManager.INSTANCE.unregisterAll() }
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteServerConfigManager.INSTANCE.initialize()
    RemoteServerResponseDispatcher.INSTANCE.initialize()
    RemoteTransportServerManager.INSTANCE.initialize()
    RemoteServiceManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RemoteServiceManager.INSTANCE.dispose()
    RemoteTransportServerManager.INSTANCE.dispose()
    RemoteServerResponseDispatcher.INSTANCE.dispose()
    RemoteServerConfigManager.INSTANCE.dispose()
  }
}