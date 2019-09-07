package org.goblinframework.embedded.core.module

import org.goblinframework.core.bootstrap.*
import org.goblinframework.embedded.core.manager.EmbeddedServerManager

class EmbeddedModule : GoblinModule {

  override fun name(): String {
    return "EMBEDDED"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.createChildModuleManager(name())
        .module("JETTY")
        .module("NETTY")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager(name())
        .module("JETTY")
        .module("NETTY")
        .bootstrap(ctx)
  }

  override fun shutdown(ctx: GoblinModuleShutdownContext) {
    ctx.createChildModuleManager(name())
        .module("JETTY")
        .module("NETTY")
        .shutdown(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager(name())
        .module("JETTY")
        .module("NETTY")
        .finalize(ctx)
    EmbeddedServerManager.INSTANCE.close()
  }
}