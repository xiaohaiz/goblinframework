package org.goblinframework.cache.redis.module.config

import org.goblinframework.api.core.CompressorMode
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean(type = "cache.redis")
class RedisConfig
internal constructor(val mapper: RedisConfigMapper)
  : GoblinManagedObject(), RedisConfigMXBean, GoblinConfig {

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

  override fun getMaxTotal(): Int {
    return mapper.maxTotal!!
  }

  override fun getMaxIdle(): Int {
    return mapper.maxIdle!!
  }

  override fun getMinIdle(): Int {
    return mapper.minIdle!!
  }

  override fun getMaxWaitMillis(): Long {
    return mapper.maxWaitMillis!!
  }

  override fun getTestOnCreate(): Boolean {
    return mapper.testOnCreate!!
  }

  override fun getTestOnBorrow(): Boolean {
    return mapper.testOnBorrow!!
  }

  override fun getTestOnReturn(): Boolean {
    return mapper.testOnReturn!!
  }

  override fun getTestWhileIdle(): Boolean {
    return mapper.testWhileIdle!!
  }

  override fun getFlushable(): Boolean {
    return mapper.flushable!!
  }

}