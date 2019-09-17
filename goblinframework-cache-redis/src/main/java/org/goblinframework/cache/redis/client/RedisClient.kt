package org.goblinframework.cache.redis.client

import org.goblinframework.cache.redis.module.config.RedisConfig
import org.goblinframework.cache.redis.transcoder.RedisTranscoder
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.serialization.SerializerManager

@GoblinManagedBean("CACHE.REDIS")
abstract class RedisClient(val config: RedisConfig)
  : GoblinManagedObject(), RedisClientMXBean {

  private val transcoder: RedisTranscoder

  init {
    val serializer = SerializerManager.INSTANCE.getSerializer(config.getSerializer())
    val compressor = config.getCompressor()?.run { CompressorManager.INSTANCE.getCompressor(this) }
    val compressionThreshold = config.getCompressionThreshold()
    transcoder = RedisTranscoder(serializer, compressor, compressionThreshold)
  }

  fun destroy() {
    unregisterIfNecessary()
    doDestroy()
    transcoder.destroy()
  }

  abstract fun doDestroy()
}