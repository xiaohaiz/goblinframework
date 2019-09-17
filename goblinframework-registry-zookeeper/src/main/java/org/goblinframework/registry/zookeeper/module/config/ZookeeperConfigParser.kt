package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigLoader
import org.goblinframework.core.serialization.SerializerMode

@Singleton
class ZookeeperConfigParser private constructor()
  : BufferedConfigParser<ZookeeperConfig>() {

  companion object {
    @JvmField val INSTANCE = ZookeeperConfigParser()
  }

  override fun initialize() {
    val mapping = ConfigLoader.INSTANCE.getMapping()
    parseToMap(mapping, "zookeeper", ZookeeperConfigMapper::class.java)
        .map { it.value.also { c -> c.name = it.key } }
        .map { ZookeeperConfig(it) }
        .forEach { putIntoBuffer(it.getName(), it) }
  }

  override fun doProcessConfig(config: ZookeeperConfig) {
    val mapper = config.mapper
    mapper.serializer ?: kotlin.run {
      mapper.serializer = SerializerMode.HESSIAN2
    }
  }
}