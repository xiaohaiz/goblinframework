package org.goblinframework.remote.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.remote.core.module.config.RemoteRegistryConfigManager
import org.goblinframework.remote.core.registry.RemoteRegistryManager

@Install
class RemoteModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.RPC
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.RPC_CLIENT)
        .next()
        .module(GoblinSubModule.RPC_SERVER)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteRegistryConfigManager.INSTANCE.initialize()
    RemoteRegistryManager.INSTANCE.initialize()
    ctx.createSubModules()
        .module(GoblinSubModule.RPC_CLIENT)
        .next()
        .module(GoblinSubModule.RPC_SERVER)
        .initialize(ctx)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.RPC_SERVER)
        .next()
        .module(GoblinSubModule.RPC_CLIENT)
        .finalize(ctx)
    RemoteRegistryManager.INSTANCE.dispose()
    RemoteRegistryConfigManager.INSTANCE.dispose()
  }
}