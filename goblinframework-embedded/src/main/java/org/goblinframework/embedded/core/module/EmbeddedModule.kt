package org.goblinframework.embedded.core.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.*
import org.goblinframework.embedded.core.manager.EmbeddedServerManager

@Install
class EmbeddedModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.EMBEDDED
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.EMBEDDED_JETTY)
        .module(GoblinSubModule.EMBEDDED_NETTY)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.EMBEDDED_JETTY)
        .module(GoblinSubModule.EMBEDDED_NETTY)
        .initialize(ctx)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.EMBEDDED_JETTY)
        .module(GoblinSubModule.EMBEDDED_NETTY)
        .finalize(ctx)
    EmbeddedServerManager.INSTANCE.dispose()
  }
}