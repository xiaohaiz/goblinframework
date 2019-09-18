package org.goblinframework.dao.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinModule

@Install
class DaoModule : GoblinModule {

  override fun name(): String {
    return "DAO"
  }

}