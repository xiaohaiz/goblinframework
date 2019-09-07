package org.goblinframework.serializer.core.module

import org.goblinframework.core.bootstrap.*

class SerializerModule : GoblinModule {

  override fun name(): String {
    return "SERIALIZER"
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