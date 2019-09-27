package org.goblinframework.dao.mysql.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.system.*
import org.goblinframework.dao.core.module.DaoModule
import org.goblinframework.dao.mysql.client.MysqlClientManager
import org.goblinframework.dao.mysql.mapping.MysqlEntityMappingBuilderProvider
import org.goblinframework.dao.mysql.module.config.MysqlConfigManager
import org.goblinframework.dao.mysql.module.test.RebuildMysqlTableBeforeTestMethod

@Install
class DaoMysqlModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.DATABASE_MYSQL
  }

  override fun install(ctx: ModuleInstallContext) {
    val parent = ctx.getExtension(DaoModule::class.java)
    parent?.registerEntityMappingBuilderProvider(MysqlEntityMappingBuilderProvider.INSTANCE)
    ctx.registerTestExecutionListener(RebuildMysqlTableBeforeTestMethod.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    MysqlConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    MysqlClientManager.INSTANCE.dispose()
    MysqlConfigManager.INSTANCE.dispose()
  }
}