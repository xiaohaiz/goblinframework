package org.goblinframework.dao.mysql.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext
import org.goblinframework.dao.mysql.client.MysqlClientManager
import org.goblinframework.dao.mysql.module.config.MysqlConfigManager
import org.goblinframework.dao.mysql.module.monitor.intruction.MSQ
import org.goblinframework.dao.mysql.module.monitor.intruction.MSQTranslator

@Install
class DaoMysqlModule : GoblinChildModule {

  override fun name(): String {
    return "DAO:MYSQL"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.registerInstructionTranslator(MSQ::class.java, MSQTranslator.INSTANCE)
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    MysqlConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    MysqlClientManager.INSTANCE.dispose()
    MysqlConfigManager.INSTANCE.dispose()
  }
}