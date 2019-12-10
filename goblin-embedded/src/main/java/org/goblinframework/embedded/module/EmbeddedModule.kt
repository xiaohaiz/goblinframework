package org.goblinframework.embedded.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.embedded.java.JavaEmbeddedServerFactory
import org.goblinframework.embedded.server.EmbeddedServerFactoryManager
import org.goblinframework.embedded.server.EmbeddedServerManager

@Install
class EmbeddedModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.EMBEDDED
  }

  override fun install(ctx: ModuleInstallContext) {
    EmbeddedServerFactoryManager.INSTANCE.register(JavaEmbeddedServerFactory.INSTANCE)
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
    EmbeddedServerFactoryManager.INSTANCE.dispose()
  }
}