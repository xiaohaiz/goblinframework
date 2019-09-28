package org.goblinframework.database.mongo.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean("DatabaseMongo")
class MongoConfigManager private constructor()
  : GoblinManagedObject(), MongoConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = MongoConfigManager()
  }

  val configParser = MongoConfigParser()

  override fun disposeBean() {
    configParser.dispose()
  }
}