package org.goblinframework.dao.mysql.client

import java.lang.management.PlatformManagedObject

interface MysqlClientMXBean : PlatformManagedObject {

  fun getMaster(): DataSourceMXBean

}