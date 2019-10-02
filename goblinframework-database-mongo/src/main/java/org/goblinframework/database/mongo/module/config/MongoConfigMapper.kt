package org.goblinframework.database.mongo.module.config

import java.io.Serializable

class MongoConfigMapper : Serializable {

  var name: String? = null
  var servers: String? = null
  var maxSize: Int? = null
  var minSize: Int? = null
  var maxWaitQueueSize: Int? = null
  var maxWaitTimeMS: Long? = null
  var maxConnectionLifeTimeMS: Long? = null
  var maxConnectionIdleTimeMS: Long? = null
  var maintenanceInitialDelayMS: Long? = null
  var maintenanceFrequencyMS: Long? = null
  var credential: MongoCredentialConfigMapper? = null

  companion object {
    private const val serialVersionUID = 8681036039310789984L
  }
}
