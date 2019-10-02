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
import org.goblinframework.database.mongo.module.test.DropMongoDatabaseBeforeTestMethod
import org.goblinframework.database.mongo.support.MongoSchedulerManager

@Install
class DatabaseMongoModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.DATABASE_MONGO
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerTestExecutionListener(DropMongoDatabaseBeforeTestMethod.INSTANCE)
    val module = ctx.getExtension(DatabaseModule::class.java)
    module?.registerEntityMappingBuilderProvider(MongoEntityMappingBuilderProvider.INSTANCE)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    MongoClientManager.INSTANCE.dispose()
    MongoConfigManager.INSTANCE.dispose()
    MongoSchedulerManager.INSTANCE.dispose()
  }
}