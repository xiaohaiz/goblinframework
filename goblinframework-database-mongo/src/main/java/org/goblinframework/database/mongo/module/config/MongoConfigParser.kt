package org.goblinframework.database.mongo.module.config

import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.config.GoblinConfigException
import org.goblinframework.core.util.StringUtils
import org.goblinframework.core.util.SystemUtils

class MongoConfigParser internal constructor()
  : BufferedConfigParser<MongoConfig>() {

  companion object {
    private const val DEFAULT_MONGO_PORT = 27017
  }

  override fun initializeBean() {
    val mapping = ConfigManager.INSTANCE.getMapping()
    parseToMap(mapping, "mongo", MongoConfigMapper::class.java)
        .map { it.value.also { c -> c.name = it.key } }
        .map { MongoConfig(it) }
        .forEach { putIntoBuffer(it.getName(), it) }
  }

  override fun doProcessConfig(config: MongoConfig) {
    val mapper = config.mapper

    mapper.servers ?: kotlin.run {
      throw GoblinConfigException("mongo.servers is required")
    }
    mapper.servers = StringUtils.formalizeServers(mapper.servers, " ") { DEFAULT_MONGO_PORT }
    mapper.stream = if (SystemUtils.isNettyFound()) "netty" else "nio2"
    mapper.maxSize ?: kotlin.run { mapper.maxSize = 100 }
    mapper.minSize ?: kotlin.run { mapper.minSize = 100 }
    mapper.maxWaitQueueSize ?: kotlin.run { mapper.maxWaitQueueSize = 500 }
    mapper.maxWaitTimeMS ?: kotlin.run { mapper.maxWaitTimeMS = 120000 }
    mapper.maxConnectionLifeTimeMS ?: kotlin.run { mapper.maxConnectionLifeTimeMS = 0 }
    mapper.maxConnectionIdleTimeMS ?: kotlin.run { mapper.maxConnectionIdleTimeMS = 0 }
    mapper.maintenanceInitialDelayMS ?: kotlin.run { mapper.maintenanceInitialDelayMS = 0 }
    mapper.maintenanceFrequencyMS ?: kotlin.run { mapper.maintenanceFrequencyMS = 60000 }
  }
}