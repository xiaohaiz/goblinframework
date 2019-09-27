package org.goblinframework.database.mongo.module

import org.goblinframework.api.core.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule

@Install
class DatabaseMongoModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.DATABASE_MONGO
  }

}