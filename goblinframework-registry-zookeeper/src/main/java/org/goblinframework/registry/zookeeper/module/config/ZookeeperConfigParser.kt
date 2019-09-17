package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigLoader
import org.goblinframework.core.exception.GoblinConfigException
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.util.StringUtils

class ZookeeperConfigParser internal constructor()
  : BufferedConfigParser<ZookeeperConfig>() {

  override fun initialize() {
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
    mapper.servers = formalizeSevers(mapper.servers!!)
    mapper.serializer ?: kotlin.run {
      mapper.serializer = SerializerMode.HESSIAN2
    }
  }

  private fun formalizeSevers(servers: String): String {
    var s = StringUtils.replace(servers, ",", " ")
    s = StringUtils.replace(s, ";", " ")
    val list = StringUtils.split(s, " ")
        .asSequence()
        .filter { StringUtils.isNotBlank(it) }
        .map { StringUtils.trim(it) }
        .map {
          if (StringUtils.contains(it, ":")) {
            it
          } else {
            "$it:2181"
          }
        }
        .distinct()
        .sorted()
        .toList()
    return StringUtils.join(list, ",")
  }
}