package org.goblinframework.cache.redis.module.client

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.cache.redis.module.config.RedisConfigManager
import org.goblinframework.cache.redis.module.config.RedisServerMode
import org.goblinframework.core.mbean.GoblinManagedObject
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
          else -> throw UnsupportedOperationException()
        }
        buffer[name] = client
        client
      }
    }
  }

  fun destroy() {
    unregisterIfNecessary()
    lock.write {
      buffer.values.forEach { it.destroy() }
      buffer.clear()
    }
  }
}