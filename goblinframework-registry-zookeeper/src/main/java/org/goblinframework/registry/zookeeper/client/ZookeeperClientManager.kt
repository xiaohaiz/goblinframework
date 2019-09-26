package org.goblinframework.registry.zookeeper.client

import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.api.core.Singleton
import org.goblinframework.api.core.ThreadSafe
import org.goblinframework.registry.zookeeper.module.config.ZookeeperConfigManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@ThreadSafe
class ZookeeperClientManager private constructor()
  : GoblinManagedObject(), ZookeeperClientManagerMXBean {

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<String, ZookeeperClient>()

  companion object {
    @JvmField val INSTANCE = ZookeeperClientManager()
  }

  fun getZookeeperClient(name: String): ZookeeperClient? {
    buffer[name]?.run { return this }
    val config = ZookeeperConfigManager.INSTANCE.getZookeeperConfig(name) ?: return null
    lock.withLock {
      buffer[name]?.run { return this }
      val client = ZookeeperClient(config)
      buffer[name] = client
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