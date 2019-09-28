package org.goblinframework.database.mongo.client

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.database.mongo.module.config.MongoConfig

@GoblinManagedBean("DatabaseMongo")
class MongoClient internal constructor(private val config: MongoConfig)
  : GoblinManagedObject(), MongoClientMXBean {

  private val nativeClient = MongoClientFactory.build(config)

  fun getNativeClient(): com.mongodb.reactivestreams.client.MongoClient {
    return nativeClient
  }

  override fun disposeBean() {
    nativeClient.close()
    logger.debug("MongoClient [${config.getName()}] disposed")
  }
}