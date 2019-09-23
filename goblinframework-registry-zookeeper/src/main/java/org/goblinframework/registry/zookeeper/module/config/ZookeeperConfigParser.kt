package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.api.config.GoblinConfigException
import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigLoader
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.util.StringUtils

class ZookeeperConfigParser internal constructor()
  : BufferedConfigParser<ZookeeperConfig>() {

  companion object {
    private const val DEFAULT_ZOOKEEPER_PORT = 2181
  }

  override fun initializeBean() {
    val mapping = ConfigLoader.INSTANCE.getMapping()
    parseToMap(mapping, "zookeeper", ZookeeperConfigMapper::class.java)
        .map { it.value.also { c -> c.name = it.key } }
        .map { ZookeeperConfig(it) }
        .forEach { putIntoBuffer(it.getName(), it) }
  }

  override fun doProcessConfig(config: ZookeeperConfig) {
    val mapper = config.mapper
    mapper.servers ?: kotlin.run {
      throw GoblinConfigException("servers is required")
    }
    mapper.servers = StringUtils.formalizeServers(mapper.servers, " ") { DEFAULT_ZOOKEEPER_PORT }
    mapper.serializer ?: kotlin.run {
      mapper.serializer = SerializerMode.HESSIAN2
    }
  }

}