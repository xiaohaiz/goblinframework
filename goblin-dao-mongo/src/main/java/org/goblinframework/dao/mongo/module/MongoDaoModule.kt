package org.goblinframework.dao.mongo.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInstallContext
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

  override fun finalize(ctx: ModuleFinalizeContext) {
    MongoClientManager.INSTANCE.dispose()
    MongoConfigManager.INSTANCE.dispose()
  }
}