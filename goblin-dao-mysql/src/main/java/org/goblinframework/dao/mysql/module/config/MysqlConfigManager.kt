package org.goblinframework.dao.mysql.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@GoblinManagedBean(type = "dao.mysql")
class MysqlConfigManager private constructor() : GoblinManagedObject(), MysqlConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = MysqlConfigManager()
  }

  private val configParser = MysqlConfigParser()

  override fun initializeBean() {
    configParser.initialize()
  }

  fun getMysqlConfigs(): List<MysqlConfig> {
    return configParser.asList()
  }

  fun getMysqlConfig(name: String): MysqlConfig? {
    return configParser.getFromBuffer(name)
  }

  override fun disposeBean() {
    configParser.dispose()
  }
}