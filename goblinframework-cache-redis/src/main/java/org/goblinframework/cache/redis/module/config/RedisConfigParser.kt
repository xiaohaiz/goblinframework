package org.goblinframework.cache.redis.module.config

import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigLoader
import org.goblinframework.core.exception.GoblinConfigException
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.util.StringUtils

class RedisConfigParser internal constructor() : BufferedConfigParser<RedisConfig>() {

  companion object {
    private const val DEFAULT_REDIS_PORT = 6379
  }

  override fun initialize() {
    val mapping = ConfigLoader.INSTANCE.getMapping()
    parseToMap(mapping, "redis", RedisConfigMapper::class.java)
        .map { it.value.also { c -> c.name = it.key } }
        .map { RedisConfig(it) }
        .forEach { putIntoBuffer(it.getName(), it) }
  }

  override fun doProcessConfig(config: RedisConfig) {
    val mapper = config.mapper
    if (StringUtils.isBlank(mapper.servers)) {
      throw GoblinConfigException("RedisConfig.servers is required")
    }
    if (mapper.mode == null) {
      throw GoblinConfigException("RedisConfig.mode is required")
    }
    val servers = StringUtils.formalizeServers(mapper.servers, " ") { DEFAULT_REDIS_PORT }
    mapper.servers = servers

    mapper.serializer ?: kotlin.run {
      mapper.serializer = SerializerMode.HESSIAN2
    }
  }
}