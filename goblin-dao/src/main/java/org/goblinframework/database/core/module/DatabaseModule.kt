package org.goblinframework.database.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.database.core.mapping.EntityMappingBuilderManager
import org.goblinframework.database.core.mapping.EntityMappingBuilderProvider

@Install
class DatabaseModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.DAO
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.DAO_MONGO)
        .module(GoblinSubModule.DAO_MYSQL)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.DAO_MONGO)
        .module(GoblinSubModule.DAO_MYSQL)
        .initialize(ctx)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.DAO_MONGO)
        .module(GoblinSubModule.DAO_MYSQL)
        .finalize(ctx)
    EntityMappingBuilderManager.INSTANCE.dispose()
  }

  fun registerEntityMappingBuilderProvider(provider: EntityMappingBuilderProvider) {
    EntityMappingBuilderManager.INSTANCE.registerEntityMappingBuilderProvider(provider)
  }
}