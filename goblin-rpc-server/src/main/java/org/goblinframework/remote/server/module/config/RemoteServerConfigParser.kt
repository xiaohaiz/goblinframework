package org.goblinframework.remote.server.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.util.SystemUtils

class RemoteServerConfigParser internal constructor() : BufferedConfigParser<RemoteServerConfig>() {

  companion object {
    internal const val CONFIG_NAME = "REMOTE_SERVER_CONFIG"
  }

  override fun initializeBean() {
    var mapper: RemoteServerConfigMapper? = null
    val mapping = ConfigManager.INSTANCE.getMapping()
    (mapping["rpc"] as? Map<*, *>)?.run {
      val remote = this
      (remote["server"] as? Map<*, *>)?.run {
        mapper = JsonMapper.getDefaultObjectMapper().convertValue(this, RemoteServerConfigMapper::class.java)
      }
    }
    mapper ?: kotlin.run { mapper = RemoteServerConfigMapper() }
    putIntoBuffer(CONFIG_NAME, RemoteServerConfig(mapper!!))
  }

  override fun doProcessConfig(config: RemoteServerConfig) {
    val mapper = config.mapper
    mapper.name ?: kotlin.run { mapper.name = "goblin.remote.server" }
    mapper.weight ?: kotlin.run { mapper.weight = SystemUtils.availableProcessors() }
    mapper.host ?: kotlin.run { mapper.host = "0.0.0.0" }
    mapper.port ?: kotlin.run { mapper.port = 0 }
    mapper.bossThreads ?: kotlin.run { mapper.bossThreads = 1 }
    mapper.workerThreads ?: kotlin.run { mapper.workerThreads = SystemUtils.availableProcessors() }
    mapper.compressionThreshold ?: kotlin.run { mapper.compressionThreshold = CompressionThreshold.NONE }
    mapper.requestBufferSize ?: kotlin.run { mapper.requestBufferSize = 32768 }
    mapper.requestBufferWorker ?: kotlin.run { mapper.requestBufferWorker = SystemUtils.availableProcessors() }
    mapper.requestConcurrent ?: kotlin.run { mapper.requestConcurrent = SystemUtils.availableProcessors() * 2 }
    mapper.responseBufferSize ?: kotlin.run { mapper.responseBufferSize = 32768 }
    mapper.responseBufferWorker ?: kotlin.run { mapper.responseBufferWorker = SystemUtils.availableProcessors() }
    mapper.maxTimeout ?: kotlin.run { mapper.maxTimeout = 900000 }
  }
}