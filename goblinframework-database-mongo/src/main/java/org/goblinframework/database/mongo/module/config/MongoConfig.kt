package org.goblinframework.database.mongo.module.config

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.config.GoblinConfig

@GoblinManagedBean(type = "DatabaseMongo")
class MongoConfig internal constructor(val mapper: MongoConfigMapper)
  : GoblinManagedObject(), GoblinConfig, MongoConfigMXBean {

  private val credentialConfigs = mutableListOf<MongoCredentialConfig>()

  init {
    mapper.credentialConfigs?.run {
      this.map { MongoCredentialConfig(it) }.forEach { credentialConfigs.add(it) }
    }
  }

  override fun getName(): String {
    return mapper.name!!
  }

  override fun getServers(): String {
    return mapper.servers!!
  }

  override fun getStream(): String {
    return mapper.stream!!
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

  override fun getCredentialConfigList(): Array<MongoCredentialConfigMXBean> {
    return credentialConfigs.toTypedArray()
  }

  override fun disposeBean() {
    credentialConfigs.forEach { it.dispose() }
  }
}