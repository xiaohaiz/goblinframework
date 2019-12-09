package org.goblinframework.dao.mongo.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.*
import org.goblinframework.dao.mongo.client.MongoClientManager
import org.goblinframework.dao.mongo.module.config.MongoConfigManager
import org.goblinframework.dao.mongo.module.test.DropDatabaseBeforeTestMethod

@Install
class MongoDaoModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.DAO_MONGO
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerTestExecutionListener(DropDatabaseBeforeTestMethod.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    MongoConfigManager.INSTANCE.initialize()
    MongoClientManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    MongoClientManager.INSTANCE.dispose()
    MongoConfigManager.INSTANCE.dispose()
  }
}