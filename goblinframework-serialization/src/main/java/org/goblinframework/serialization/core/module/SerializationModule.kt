package org.goblinframework.serialization.core.module

import org.goblinframework.core.bootstrap.*

class SerializationModule : GoblinModule {

  override fun name(): String {
    return "SERIALIZATION"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.createChildModuleManager(name())
        .module("HESSIAN")
        .module("FST")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager(name())
        .module("HESSIAN")
        .module("FST")
        .bootstrap(ctx)
  }

  override fun shutdown(ctx: GoblinModuleShutdownContext) {
    ctx.createChildModuleManager(name())
        .module("HESSIAN")
        .module("FST")
        .shutdown(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager(name())
        .module("HESSIAN")
        .module("FST")
        .finalize(ctx)
  }
}