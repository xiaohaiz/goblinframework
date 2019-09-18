package org.goblinframework.dao.mysql.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.dao.mysql.client.MysqlClientManager
import org.goblinframework.dao.mysql.module.config.MysqlConfigManager

@Install
class DaoMysqlModule : GoblinChildModule {

  override fun name(): String {
    return "DAO:MYSQL"
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    MysqlConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    MysqlClientManager.INSTANCE.destroy()
    MysqlConfigManager.INSTANCE.destroy()
  }
}