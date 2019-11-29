package org.goblinframework.remote.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.remote.core.module.config.RemoteRegistryConfigManager

@Install
class RemoteModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.REMOTE
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.REMOTE_CLIENT)
        .next()
        .module(GoblinSubModule.REMOTE_SERVER)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RemoteRegistryConfigManager.INSTANCE.initialize()
    ctx.createSubModules()
        .module(GoblinSubModule.REMOTE_CLIENT)
        .next()
        .module(GoblinSubModule.REMOTE_SERVER)
        .initialize(ctx)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.REMOTE_SERVER)
        .next()
        .module(GoblinSubModule.REMOTE_CLIENT)
        .finalize(ctx)
    RemoteRegistryConfigManager.INSTANCE.dispose()
  }
}