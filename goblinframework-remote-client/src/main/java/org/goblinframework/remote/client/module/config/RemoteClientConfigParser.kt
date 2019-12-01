package org.goblinframework.remote.client.module.config

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.mapper.JsonMapper

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
    mapper.serializer ?: kotlin.run { mapper.serializer = SerializerMode.HESSIAN2 }
    mapper.compressionThreshold ?: kotlin.run { mapper.compressionThreshold = CompressionThreshold.NONE }
  }
}