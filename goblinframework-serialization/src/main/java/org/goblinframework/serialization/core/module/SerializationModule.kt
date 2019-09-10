package org.goblinframework.serialization.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.serialization.core.manager.SerializerManager
import org.goblinframework.serialization.core.module.management.SerializationManagementController

class SerializationModule : GoblinModule {

  override fun name(): String {
    return "SERIALIZATION"
  }

  override fun managementEntrance(): String? {
    return "/serialization/index.do"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.registerManagementController(SerializationManagementController.INSTANCE)
    ctx.createChildModuleManager()
        .module("SERIALIZATION:FST")
        .module("SERIALIZATION:HESSIAN")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    SerializerManager.INSTANCE.initialize()
    ctx.createChildModuleManager()
        .module("SERIALIZATION:FST")
        .module("SERIALIZATION:HESSIAN")
        .bootstrap(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager()
        .module("SERIALIZATION:FST")
        .module("SERIALIZATION:HESSIAN")
        .finalize(ctx)
    SerializerManager.INSTANCE.close()
  }
}