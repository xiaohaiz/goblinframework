package org.goblinframework.dao.mongo.module.config

import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean(type = "DatabaseMongo")
class MongoConfig internal constructor(val mapper: MongoConfigMapper)
  : GoblinManagedObject(), GoblinConfig, MongoConfigMXBean {

  private val credential: MongoCredentialConfig?

  init {
    credential = mapper.credential?.run { MongoCredentialConfig(this) }
  }

  override fun getName(): String {
    return mapper.name!!
  }

  override fun getServers(): String {
    return mapper.servers!!
  }

  override fun getMaxSize(): Int {
    return mapper.maxSize!!
  }

  override fun getMinSize(): Int {
    return mapper.minSize!!
  }

  override fun getMaxWaitQueueSize(): Int {
    return mapper.maxWaitQueueSize!!
  }

  override fun getMaxWaitTimeMS(): Long {
    return mapper.maxWaitTimeMS!!
  }

  override fun getMaxConnectionLifeTimeMS(): Long {
    return mapper.maxConnectionLifeTimeMS!!
  }

  override fun getMaxConnectionIdleTimeMS(): Long {
    return mapper.maxConnectionIdleTimeMS!!
  }

  override fun getMaintenanceInitialDelayMS(): Long {
    return mapper.maintenanceInitialDelayMS!!
  }

  override fun getMaintenanceFrequencyMS(): Long {
    return mapper.maintenanceFrequencyMS!!
  }

  override fun getCredential(): MongoCredentialConfig? {
    return credential
  }

  override fun disposeBean() {
    credential?.dispose()
  }
}