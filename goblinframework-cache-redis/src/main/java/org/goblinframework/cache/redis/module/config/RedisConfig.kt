package org.goblinframework.cache.redis.module.config

import org.goblinframework.cache.redis.module.RedisServerMode
import org.goblinframework.core.config.Config
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject

@GoblinManagedBean("CACHE.REDIS")
class RedisConfig
internal constructor(val mapper: RedisConfigMapper)
  : GoblinManagedObject(), RedisConfigMXBean, Config {

  override fun getName(): String {
    return mapper.name!!
  }

  override fun getServers(): String {
    return mapper.servers!!
  }

  override fun getPassword(): String? {
    throw UnsupportedOperationException()
  }

  override fun getMode(): RedisServerMode {
    return mapper.mode!!
  }

  override fun destroy() {
    unregisterIfNecessary()
  }
}