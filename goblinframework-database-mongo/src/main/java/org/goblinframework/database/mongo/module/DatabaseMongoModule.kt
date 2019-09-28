package org.goblinframework.database.mongo.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule

@Install
class DatabaseMongoModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.DATABASE_MONGO
  }

}