package org.goblinframework.dao.mysql.module.config

import java.lang.management.PlatformManagedObject

interface MysqlConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getMaster(): DataSourceConfigMXBean

  fun getSlaveList(): Array<DataSourceConfigMXBean>

}