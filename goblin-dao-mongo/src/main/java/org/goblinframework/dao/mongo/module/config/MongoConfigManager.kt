package org.goblinframework.dao.mongo.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean("MongoDao")
class MongoConfigManager private constructor() : GoblinManagedObject(), MongoConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = MongoConfigManager()
  }

  private val configParser = MongoConfigParser()

  override fun initializeBean() {
    configParser.initialize()
  }

  fun getMongoConfig(name: String): MongoConfig? {
    return configParser.getFromBuffer(name)
  }

  fun getMongoConfigs(): List<MongoConfig> {
    return configParser.asList()
  }

  override fun getMongoConfigList(): Array<MongoConfigMXBean> {
    return getMongoConfigs().toTypedArray()
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}