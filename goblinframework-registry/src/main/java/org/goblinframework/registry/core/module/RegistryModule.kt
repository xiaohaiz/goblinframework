package org.goblinframework.registry.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext

@Install
class RegistryModule : GoblinModule {

  override fun name(): String {
    return "REGISTRY"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.createChildModuleManager()
        .module("REGISTRY:ZOOKEEPER")
        .install(ctx)
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ctx.createChildModuleManager()
        .module("REGISTRY:ZOOKEEPER")
        .initialize(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager()
        .module("REGISTRY:ZOOKEEPER")
        .finalize(ctx)
  }
}