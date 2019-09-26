package org.goblinframework.dao.mysql.module.config

import org.goblinframework.api.config.GoblinConfig
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject

@GoblinManagedBean(type = "dao.mysql")
class MysqlConfig(private val name: String,
                  private val master: DataSourceConfig,
                  private val slaves: List<DataSourceConfig>)
  : GoblinManagedObject(), MysqlConfigMXBean, GoblinConfig {

  override fun disposeBean() {
    master.dispose()
    slaves.forEach { it.dispose() }
  }

  override fun getName(): String {
    return name
  }

  override fun getMaster(): DataSourceConfigMXBean {
    return master
  }

  override fun getSlaveList(): Array<DataSourceConfigMXBean> {
    return slaves.toTypedArray()
  }
}