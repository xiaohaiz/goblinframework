package org.goblinframework.remote.core.module.config

import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.config.GoblinConfigException
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.util.StringUtils

class RemoteRegistryConfigParser internal constructor() : BufferedConfigParser<RemoteRegistryConfig>() {

  companion object {
    internal const val CONFIG_NAME = "REMOTE_REGISTRY_CONFIG"
    private const val DEFAULT_ZOOKEEPER_PORT = 2181
  }

  override fun initializeBean() {
    val mapping = ConfigManager.INSTANCE.getMapping()
    (mapping["remote"] as? Map<*, *>)?.run {
      val remote = this
      (remote["registry"] as? Map<*, *>)?.run {
        val registry = this
        val mapper = JsonMapper.getDefaultObjectMapper().convertValue(registry, RemoteRegistryConfigMapper::class.java)
        putIntoBuffer(CONFIG_NAME, RemoteRegistryConfig(mapper))
      }
    }
  }

  override fun doProcessConfig(config: RemoteRegistryConfig) {
    val mapper = config.mapper

    mapper.zookeeper ?: kotlin.run {
      throw GoblinConfigException("remote.registry.zookeeper is required")
    }
    mapper.zookeeper = StringUtils.formalizeServers(mapper.zookeeper, ",") { DEFAULT_ZOOKEEPER_PORT }
    mapper.connectionTimeout ?: kotlin.run { mapper.connectionTimeout = Int.MAX_VALUE }
    mapper.sessionTimeout ?: kotlin.run { mapper.sessionTimeout = 30000 }
    mapper.serializer ?: kotlin.run { mapper.serializer = SerializerMode.JAVA }
  }
}