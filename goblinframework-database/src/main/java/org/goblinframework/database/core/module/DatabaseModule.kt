package org.goblinframework.database.core.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.system.*
import org.goblinframework.database.core.mapping.EntityMappingBuilderManager
import org.goblinframework.database.core.mapping.EntityMappingBuilderProvider

@Install
class DatabaseModule : IModule {

  override fun id(): GoblinModule {
    return GoblinModule.DATABASE
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.DATABASE_MONGO)
        .module(GoblinSubModule.DATABASE_MYSQL)
        .install(ctx)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.DATABASE_MONGO)
        .module(GoblinSubModule.DATABASE_MYSQL)
        .initialize(ctx)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ctx.createSubModules()
        .module(GoblinSubModule.DATABASE_MONGO)
        .module(GoblinSubModule.DATABASE_MYSQL)
        .finalize(ctx)
    EntityMappingBuilderManager.INSTANCE.dispose()
  }

  fun registerEntityMappingBuilderProvider(provider: EntityMappingBuilderProvider) {
    EntityMappingBuilderManager.INSTANCE.registerEntityMappingBuilderProvider(provider)
  }
}