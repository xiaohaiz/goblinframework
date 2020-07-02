package org.goblinframework.rpc.module.config

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.mapper.JsonMapper
import org.goblinframework.core.util.StringUtils
import org.goblinframework.rpc.exception.RpcConfigException

class RpcRegistryConfigParser internal constructor() : BufferedConfigParser<RpcRegistryConfig>() {

  companion object {
    internal const val CONFIG_NAME = "RPC_REGISTRY_CONFIG"
    private const val DEFAULT_ZOOKEEPER_PORT = 2181
  }

  override fun initializeBean() {
    val mapping = ConfigManager.INSTANCE.getMapping()
    (mapping["rpc"] as? Map<*, *>)?.run {
      val rpc = this
      (rpc["registry"] as? Map<*, *>)?.run {
        val registry = this
        val mapper = JsonMapper.getDefaultObjectMapper().convertValue(registry, RpcRegistryConfigMapper::class.java)
        putIntoBuffer(CONFIG_NAME, RpcRegistryConfig(mapper))
      }
    }
  }

  override fun doProcessConfig(config: RpcRegistryConfig) {
    val mapper = config.mapper
    mapper.zookeeper ?: kotlin.run {
      throw RpcConfigException("remote.registry.zookeeper is required")
    }
    mapper.zookeeper = StringUtils.formalizeServers(mapper.zookeeper, ",") { DEFAULT_ZOOKEEPER_PORT }
    mapper.connectionTimeout ?: kotlin.run { mapper.connectionTimeout = Int.MAX_VALUE }
    mapper.sessionTimeout ?: kotlin.run { mapper.sessionTimeout = 30000 }
    mapper.serializer ?: kotlin.run { mapper.serializer = SerializerMode.JAVA }
  }
}