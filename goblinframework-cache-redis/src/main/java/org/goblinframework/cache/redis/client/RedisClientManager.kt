package org.goblinframework.cache.redis.client

import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.api.core.Singleton
import org.goblinframework.api.core.ThreadSafe
import org.goblinframework.cache.redis.module.config.RedisConfigManager
import org.goblinframework.cache.redis.module.config.RedisServerMode
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
class RedisClientManager private constructor() : GoblinManagedObject(), RedisClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RedisClientManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<String, RedisClient>()

  fun getRedisClient(name: String): RedisClient? {
    val config = RedisConfigManager.INSTANCE.getRedisConfig(name) ?: return null
    return lock.read { buffer[name] } ?: lock.write {
      buffer[name] ?: kotlin.run {
        val client = when (config.getMode()) {
          RedisServerMode.SINGLE -> SingleRedisClient(config)
          RedisServerMode.CLUSTER -> ClusterRedisClient(config)
        }
        buffer[name] = client
        client
      }
    }
  }

  fun closeRedisClient(name: String) {
    lock.write { buffer.remove(name) }?.dispose()
  }

  override fun disposeBean() {
    lock.write {
      buffer.values.forEach { it.dispose() }
      buffer.clear()
    }
  }
}