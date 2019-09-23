package org.goblinframework.remote.server.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInitializeContext
import org.goblinframework.remote.server.handler.RemoteServer
import org.goblinframework.remote.server.service.RemoteServiceManager

@Install
class RemoteServerModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_SERVER
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteServer.INSTANCE.start()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RemoteServer.INSTANCE.dispose()
    RemoteServiceManager.INSTANCE.dispose()
  }
}