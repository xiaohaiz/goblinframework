package org.goblinframework.cache.redis.module.config

import org.goblinframework.cache.redis.module.RedisServerMode
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

}