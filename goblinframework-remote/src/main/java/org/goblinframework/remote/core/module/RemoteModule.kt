package org.goblinframework.remote.core.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.*

@Install
class RemoteModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.REMOTE
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.createSubModules(this)
        .module(GoblinSubModule.REMOTE_CLIENT)
        .next()
        .module(GoblinSubModule.REMOTE_SERVER)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ctx.createSubModules(this)
        .module(GoblinSubModule.REMOTE_CLIENT)
        .next()
        .module(GoblinSubModule.REMOTE_SERVER)
        .initialize(ctx)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ctx.createSubModules(this)
        .module(GoblinSubModule.REMOTE_SERVER)
        .next()
        .module(GoblinSubModule.REMOTE_CLIENT)
        .finalize(ctx)
  }
}