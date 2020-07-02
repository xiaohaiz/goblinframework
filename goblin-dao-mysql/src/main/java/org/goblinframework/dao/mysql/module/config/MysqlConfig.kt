package org.goblinframework.dao.mysql.module.config

import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean(type = "MysqlDao")
class MysqlConfig(private val name: String,
                  private val master: DataSourceConfig,
                  private val slaves: List<DataSourceConfig>,
                  private val autoInit: Boolean)
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

  override fun getAutoInit(): Boolean {
    return autoInit
  }
}