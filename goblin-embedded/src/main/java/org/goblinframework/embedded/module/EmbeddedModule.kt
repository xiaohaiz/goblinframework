package org.goblinframework.embedded.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.embedded.module.management.EmbeddedManagement
import org.goblinframework.embedded.server.EmbeddedServerFactoryManager
import org.goblinframework.embedded.server.EmbeddedServerManager
import org.goblinframework.embedded.server.internal.JavaEmbeddedServerFactory

@Install
class EmbeddedModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.EMBEDDED
  }

  override fun managementEntrance(): String? {
    return if (EmbeddedServerManager.INSTANCE.getEmbeddedServerList().isNotEmpty())
      "/goblin/embedded/index.do" else null
  }

  override fun install(ctx: ModuleInstallContext) {
    EmbeddedServerFactoryManager.INSTANCE.register(JavaEmbeddedServerFactory.INSTANCE)
    ctx.registerManagementController(EmbeddedManagement.INSTANCE)
    ctx.createSubModules()
        .module(GoblinSubModule.EMBEDDED_JETTY)
        .module(GoblinSubModule.EMBEDDED_NETTY)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    EmbeddedServerManager.INSTANCE.initialize()
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