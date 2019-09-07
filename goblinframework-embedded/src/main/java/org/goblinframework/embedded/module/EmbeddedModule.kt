package org.goblinframework.embedded.module

import org.goblinframework.core.bootstrap.*

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
  }
}