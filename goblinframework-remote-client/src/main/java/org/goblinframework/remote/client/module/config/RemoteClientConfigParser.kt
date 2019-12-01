package org.goblinframework.remote.client.module.config

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.util.SystemUtils

class RemoteClientConfigParser internal constructor()
  : BufferedConfigParser<RemoteClientConfig>() {

  companion object {
    internal const val CONFIG_NAME = "REMOTE_CLIENT_CONFIG"
  }

  override fun initializeBean() {
    var mapper: RemoteClientConfigMapper? = null
    val mapping = ConfigManager.INSTANCE.getMapping()
    (mapping["remote"] as? Map<*, *>)?.run {
      val remote = this
      (remote["client"] as? Map<*, *>)?.run {
        val client = this
        mapper = JsonMapper.getDefaultObjectMapper().convertValue(client, RemoteClientConfigMapper::class.java)
      }
    }
    mapper ?: kotlin.run { mapper = RemoteClientConfigMapper() }
    putIntoBuffer(CONFIG_NAME, RemoteClientConfig(mapper!!))
  }

  override fun doProcessConfig(config: RemoteClientConfig) {
    val mapper = config.mapper
    mapper.workerThreads ?: kotlin.run { mapper.workerThreads = 2 }
    mapper.sendHeartbeat ?: kotlin.run { mapper.sendHeartbeat = false }
    mapper.maxTimeout ?: kotlin.run { mapper.maxTimeout = 900000 }
    mapper.serializer ?: kotlin.run { mapper.serializer = SerializerMode.HESSIAN2 }
    mapper.compressionThreshold ?: kotlin.run { mapper.compressionThreshold = CompressionThreshold.NONE }
    mapper.requestBufferSize ?: kotlin.run { mapper.requestBufferSize = 32768 }
    mapper.requestBufferWorker ?: kotlin.run { mapper.requestBufferWorker = SystemUtils.availableProcessors() }
    mapper.requestConcurrent ?: kotlin.run { mapper.requestConcurrent = SystemUtils.availableProcessors() * 2 }
    mapper.responseBufferSize ?: kotlin.run { mapper.responseBufferSize = 32768 }
    mapper.responseBufferWorker ?: kotlin.run { mapper.responseBufferWorker = SystemUtils.availableProcessors() }
  }
}