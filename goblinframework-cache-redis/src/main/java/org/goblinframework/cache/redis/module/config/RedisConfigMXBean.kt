package org.goblinframework.cache.redis.module.config

import org.goblinframework.cache.redis.module.RedisServerMode
import java.lang.management.PlatformManagedObject

interface RedisConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getServers(): String

  fun getPassword(): String?

  fun getMode(): RedisServerMode

}