package org.goblinframework.embedded.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext
import org.goblinframework.embedded.core.manager.EmbeddedServerManager

class EmbeddedModule : GoblinModule {

  override fun name(): String {
    return "EMBEDDED"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.createChildModuleManager()
        .module("EMBEDDED:JETTY")
        .module("EMBEDDED:NETTY")
        .initialize(ctx)
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.createChildModuleManager()
        .module("EMBEDDED:JETTY")
        .module("EMBEDDED:NETTY")
        .bootstrap(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager()
        .module("EMBEDDED:JETTY")
        .module("EMBEDDED:NETTY")
        .finalize(ctx)
    EmbeddedServerManager.INSTANCE.dispose()
  }
}