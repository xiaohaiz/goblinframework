package org.goblinframework.serialization.core.module

import org.goblinframework.core.bootstrap.*
import org.goblinframework.serialization.core.manager.SerializerManager

class SerializationModule : GoblinModule {

  override fun name(): String {
    return "SERIALIZATION"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    SerializerManager.INSTANCE.initialize()
    ctx.createChildModuleManager()
        .module("SERIALIZATION:FST")
        .module("SERIALIZATION:HESSIAN")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager()
        .module("SERIALIZATION:FST")
        .module("SERIALIZATION:HESSIAN")
        .bootstrap(ctx)
  }

  override fun shutdown(ctx: GoblinModuleShutdownContext) {
    ctx.createChildModuleManager()
        .module("SERIALIZATION:FST")
        .module("SERIALIZATION:HESSIAN")
        .shutdown(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager()
        .module("SERIALIZATION:FST")
        .module("SERIALIZATION:HESSIAN")
        .finalize(ctx)
    SerializerManager.INSTANCE.close()
  }
}