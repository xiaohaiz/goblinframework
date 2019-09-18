package org.goblinframework.dao.mysql.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule

@Install
class DaoMysqlModule : GoblinChildModule {

  override fun name(): String {
    return "DAO:MYSQL"
  }
}