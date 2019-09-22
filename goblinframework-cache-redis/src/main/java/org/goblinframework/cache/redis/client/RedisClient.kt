package org.goblinframework.cache.redis.client

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.cache.redis.command.RedisCommands
import org.goblinframework.cache.redis.connection.RedisConnection
import org.goblinframework.cache.redis.connection.TransactionCallback
import org.goblinframework.cache.redis.module.config.RedisConfig
import org.goblinframework.cache.redis.transcoder.RedisTranscoder
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.serialization.SerializerManager

@GoblinManagedBean(type = "cache.redis")
abstract class RedisClient(val config: RedisConfig)
  : GoblinManagedObject(), RedisClientMXBean {

  private val transcoder: RedisTranscoder
  private val poolConfig: GenericObjectPoolConfig<RedisConnection>

  init {
    val serializer = SerializerManager.INSTANCE.getSerializer(config.getSerializer())
    val compressor = config.getCompressor()?.run { CompressorManager.INSTANCE.getCompressor(this) }
    val compressionThreshold = config.getCompressionThreshold()
    transcoder = RedisTranscoder(serializer, compressor, compressionThreshold)

    poolConfig = GenericObjectPoolConfig()
    poolConfig.maxTotal = config.getMaxTotal()
    poolConfig.maxIdle = config.getMaxIdle()
    poolConfig.minIdle = config.getMinIdle()
    poolConfig.maxWaitMillis = config.getMaxWaitMillis()
    poolConfig.testOnCreate = config.getTestOnCreate()
    poolConfig.testOnBorrow = config.getTestOnBorrow()
    poolConfig.testOnReturn = config.getTestOnReturn()
    poolConfig.testWhileIdle = config.getTestWhileIdle()
    poolConfig.jmxEnabled = false
  }

  fun getTranscoder(): RedisTranscoder {
    return transcoder
  }

  fun getPoolConfig(): GenericObjectPoolConfig<RedisConnection> {
    return poolConfig
  }

  override fun disposeBean() {
    doDispose()
    transcoder.dispose()
  }

  fun flush() {
    if (config.getFlushable()) {
      doFlush()
    }
  }

  abstract fun doFlush()

  abstract fun doDispose()

  abstract fun getConnection(): RedisConnection

  abstract fun openPooledConnection(): RedisConnection

  abstract fun returnPooledConnection(connection: RedisConnection)

  abstract fun <E> executeTransaction(key: String, callback: TransactionCallback<E>): E

  abstract fun getRedisCommands(): RedisCommands
}