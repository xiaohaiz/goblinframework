package org.goblinframework.transport.core.module

import org.goblinframework.core.bootstrap.*

class TransportModule : GoblinModule {

  override fun name(): String {
    return "TRANSPORT"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.createChildModuleManager()
        .module("TRANSPORT:CLIENT")
        .next()
        .module("TRANSPORT:SERVER")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager()
        .module("TRANSPORT:CLIENT")
        .next()
        .module("TRANSPORT:SERVER")
        .bootstrap(ctx)
  }

  override fun shutdown(ctx: GoblinModuleShutdownContext) {
    ctx.createChildModuleManager()
        .module("TRANSPORT:SERVER")
        .next()
        .module("TRANSPORT:CLIENT")
        .shutdown(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager()
        .module("TRANSPORT:SERVER")
        .next()
        .module("TRANSPORT:CLIENT")
        .finalize(ctx)
  }
}