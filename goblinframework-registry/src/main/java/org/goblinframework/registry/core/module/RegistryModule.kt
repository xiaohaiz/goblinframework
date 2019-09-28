package org.goblinframework.registry.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.registry.RegistryBuilder
import org.goblinframework.api.registry.RegistrySystem
import org.goblinframework.core.system.*
import org.goblinframework.registry.core.manager.RegistryBuilderManager

@Install
class RegistryModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.REGISTRY
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.REGISTRY_ZOOKEEPER)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.REGISTRY_ZOOKEEPER)
        .initialize(ctx)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RegistryBuilderManager.INSTANCE.dispose()
    ctx.createSubModules()
        .module(GoblinSubModule.REGISTRY_ZOOKEEPER)
        .finalize(ctx)
  }

  fun registerRegistryBuilder(system: RegistrySystem, builder: RegistryBuilder) {
    RegistryBuilderManager.INSTANCE.register(system, builder)
  }
}