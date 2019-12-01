package org.goblinframework.cache.redis.module.config

import org.goblinframework.api.core.CompressorMode
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import java.lang.management.PlatformManagedObject

interface RedisConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getServers(): String

  fun getPassword(): String?

  fun getMode(): RedisServerMode

  fun getSerializer(): SerializerMode

  fun getCompressor(): CompressorMode?

  fun getCompressionThreshold(): CompressionThreshold?

  fun getMaxTotal(): Int

  fun getMaxIdle(): Int

  fun getMinIdle(): Int

  fun getMaxWaitMillis(): Long

  fun getTestOnCreate(): Boolean

  fun getTestOnBorrow(): Boolean

  fun getTestOnReturn(): Boolean

  fun getTestWhileIdle(): Boolean

  fun getFlushable(): Boolean

}