package org.goblinframework.database.mongo.module.config

import org.goblinframework.api.config.GoblinConfig
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject

@GoblinManagedBean(type = "DatabaseMongo")
class MongoConfig internal constructor(val mapper: MongoConfigMapper)
  : GoblinManagedObject(), GoblinConfig, MongoConfigMXBean {

  override fun getName(): String {
    return mapper.name!!
  }
}