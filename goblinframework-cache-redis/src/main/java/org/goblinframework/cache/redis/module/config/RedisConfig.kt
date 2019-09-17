package org.goblinframework.cache.redis.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.config.Config
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.serialization.SerializerMode

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

  override fun getSerializer(): SerializerMode {
    return mapper.serializer!!
  }

  override fun getCompressor(): CompressorMode? {
    return mapper.compressor
  }

  override fun getCompressionThreshold(): CompressionThreshold? {
    return mapper.compressionThreshold
  }

  override fun destroy() {
    unregisterIfNecessary()
  }
}