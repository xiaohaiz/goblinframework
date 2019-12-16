package org.goblinframework.rpc.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.remote.core.registry.RemoteRegistryManager
import org.goblinframework.rpc.module.config.RpcRegistryConfigManager

@Install
class RpcModule : IModule {

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
    RpcRegistryConfigManager.INSTANCE.initialize()
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
    RpcRegistryConfigManager.INSTANCE.dispose()
  }
}