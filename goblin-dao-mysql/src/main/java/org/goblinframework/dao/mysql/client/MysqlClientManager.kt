package org.goblinframework.dao.mysql.client

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.dao.mysql.module.config.MysqlConfigManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "dao.mysql")
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

  override fun disposeBean() {
    lock.withLock {
      buffer.values.forEach { it.dispose() }
      buffer.clear()
    }
  }
}