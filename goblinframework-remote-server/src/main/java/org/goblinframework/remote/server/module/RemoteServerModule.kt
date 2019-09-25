package org.goblinframework.remote.server.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.*
import org.goblinframework.remote.server.expose.ExposeSpringContainer
import org.goblinframework.remote.server.handler.RemoteServer
import org.goblinframework.remote.server.handler.RemoteServerEventListener
import org.goblinframework.remote.server.service.RemoteServiceManager

@Install
class RemoteServerModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_SERVER
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerEventChannel("/goblin/remote/server", 32768, 0)
    ctx.subscribeEventListener(ExposeSpringContainer.INSTANCE)
    ctx.subscribeEventListener(RemoteServerEventListener.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteServer.INSTANCE.start()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RemoteServer.INSTANCE.dispose()
    RemoteServiceManager.INSTANCE.dispose()
  }
}