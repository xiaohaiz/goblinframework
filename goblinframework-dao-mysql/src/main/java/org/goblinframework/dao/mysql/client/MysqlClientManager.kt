package org.goblinframework.dao.mysql.client

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.dao.mysql.module.config.MysqlConfigManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@ThreadSafe
@GoblinManagedBean("DAO.MYSQL")
class MysqlClientManager : GoblinManagedObject(), MysqlClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = MysqlClientManager()
  }

  private val buffer = ConcurrentHashMap<String, MysqlClient>()
  private val lock = ReentrantLock()

  fun getMysqlClient(name: String): MysqlClient? {
    val config = MysqlConfigManager.INSTANCE.getMysqlConfig(name) ?: return null
    val id = config.getName()
    var cached = buffer[id]
    if (cached != null) return cached
    lock.withLock {
      cached = buffer[id]
      if (cached != null) return cached
      val client = MysqlClient(config)
      buffer[id] = client
      return client
    }
  }

  fun destroy() {
    unregisterIfNecessary()
    lock.withLock {
      buffer.values.forEach { it.destroy() }
      buffer.clear()
    }
  }
}