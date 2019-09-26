package org.goblinframework.dao.mysql.module.config

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject

@Singleton
@GoblinManagedBean(type = "dao.mysql")
class MysqlConfigManager private constructor() : GoblinManagedObject(), MysqlConfigManagerMXBean {

  companion object {
    @JvmField val INSTANCE = MysqlConfigManager()
  }

  private val configParser = MysqlConfigParser()

  init {
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