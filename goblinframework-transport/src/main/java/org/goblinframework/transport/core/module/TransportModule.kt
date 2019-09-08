package org.goblinframework.transport.core.module

import org.goblinframework.core.bootstrap.*

class TransportModule : GoblinModule {

  override fun name(): String {
    return "TRANSPORT"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.createChildModuleManager(name())
        .module("CLIENT")
        .next()
        .module("SERVER")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager(name())
        .module("CLIENT")
        .next()
        .module("SERVER")
        .bootstrap(ctx)
  }

  override fun shutdown(ctx: GoblinModuleShutdownContext) {
    ctx.createChildModuleManager(name())
        .module("SERVER")
        .next()
        .module("CLIENT")
        .shutdown(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager(name())
        .module("SERVER")
        .next()
        .module("CLIENT")
        .finalize(ctx)
  }
}