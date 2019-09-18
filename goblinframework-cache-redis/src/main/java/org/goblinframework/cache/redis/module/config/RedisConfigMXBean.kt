package org.goblinframework.cache.redis.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.serialization.SerializerMode
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