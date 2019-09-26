package org.goblinframework.remote.server.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.*
import org.goblinframework.remote.server.handler.RemoteServerEventListener
import org.goblinframework.remote.server.handler.RemoteServerManager
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import org.goblinframework.remote.server.module.management.RemoteServerManagement
import org.goblinframework.remote.server.service.ExposeSpringContainer
import org.goblinframework.remote.server.service.RemoteServiceManager

@Install
class RemoteServerModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REMOTE_SERVER
  }

  override fun managementEntrance(): String? {
    return "/goblin/remote/server/index.do"
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerPriorFinalizationTask { RemoteServiceManager.INSTANCE.unregisterAll() }
    ctx.registerEventChannel("/goblin/remote/server", 32768, 0)
    ctx.subscribeEventListener(ExposeSpringContainer.INSTANCE)
    ctx.subscribeEventListener(RemoteServerEventListener.INSTANCE)
    ctx.registerConfigParser(RemoteServerConfigManager.INSTANCE.configParser)
    ctx.registerManagementController(RemoteServerManagement.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteServerManager.INSTANCE.initialize()
    RemoteServiceManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RemoteServiceManager.INSTANCE.dispose()
    RemoteServerManager.INSTANCE.dispose()
  }
}