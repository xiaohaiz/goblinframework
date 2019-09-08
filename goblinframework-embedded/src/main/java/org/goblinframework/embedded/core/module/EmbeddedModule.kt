package org.goblinframework.embedded.core.module

import org.goblinframework.core.bootstrap.*
import org.goblinframework.embedded.core.manager.EmbeddedServerManager

class EmbeddedModule : GoblinModule {

  override fun name(): String {
    return "EMBEDDED"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.createChildModuleManager()
        .module("EMBEDDED:JETTY")
        .module("EMBEDDED:NETTY")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager()
        .module("EMBEDDED:JETTY")
        .module("EMBEDDED:NETTY")
        .bootstrap(ctx)
  }

  override fun shutdown(ctx: GoblinModuleShutdownContext) {
    ctx.createChildModuleManager()
        .module("EMBEDDED:JETTY")
        .module("EMBEDDED:NETTY")
        .shutdown(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager()
        .module("EMBEDDED:JETTY")
        .module("EMBEDDED:NETTY")
        .finalize(ctx)
    EmbeddedServerManager.INSTANCE.close()
  }
}