package org.goblinframework.database.mongo.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInstallContext
import org.goblinframework.database.core.module.DatabaseModule
import org.goblinframework.database.mongo.client.MongoClientManager
import org.goblinframework.database.mongo.mapping.MongoEntityMappingBuilderProvider
import org.goblinframework.database.mongo.module.config.MongoConfigManager

@Install
class DatabaseMongoModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.DATABASE_MONGO
  }

  override fun install(ctx: ModuleInstallContext) {
    val module = ctx.getExtension(DatabaseModule::class.java)
    module?.registerEntityMappingBuilderProvider(MongoEntityMappingBuilderProvider.INSTANCE)
    ctx.registerConfigParser(MongoConfigManager.INSTANCE.configParser)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    MongoClientManager.INSTANCE.dispose()
    MongoConfigManager.INSTANCE.dispose()
  }
}