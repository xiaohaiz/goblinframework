package org.goblinframework.dao.mysql.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.system.*
import org.goblinframework.dao.mysql.client.MysqlClientManager
import org.goblinframework.dao.mysql.module.config.MysqlConfigManager
import org.goblinframework.dao.mysql.module.test.RebuildMysqlTableBeforeTestMethod

@Install
class DaoMysqlModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.DAO_MYSQL
  }

  override fun install(ctx: ModuleInstallContext) {
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