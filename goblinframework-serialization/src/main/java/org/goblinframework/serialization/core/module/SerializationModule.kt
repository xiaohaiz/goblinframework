package org.goblinframework.serialization.core.module

import org.goblinframework.core.bootstrap.*
import org.goblinframework.serialization.core.manager.SerializerManager

class SerializationModule : GoblinModule {

  override fun name(): String {
    return "SERIALIZATION"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    SerializerManager.INSTANCE.initialize()
    ctx.createChildModuleManager(name())
        .module("FST")
        .module("HESSIAN")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager(name())
        .module("FST")
        .module("HESSIAN")
        .bootstrap(ctx)
  }

  override fun shutdown(ctx: GoblinModuleShutdownContext) {
    ctx.createChildModuleManager(name())
        .module("FST")
        .module("HESSIAN")
        .shutdown(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager(name())
        .module("FST")
        .module("HESSIAN")
        .finalize(ctx)
    SerializerManager.INSTANCE.close()
  }
}