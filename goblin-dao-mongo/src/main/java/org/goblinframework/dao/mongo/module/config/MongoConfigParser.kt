package org.goblinframework.dao.mongo.module.config

import org.goblinframework.core.config.BufferedConfigParser
import org.goblinframework.core.config.ConfigManager
import org.goblinframework.core.util.StringUtils
import org.goblinframework.dao.mongo.exception.GoblinMongoConfigException

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
    mapper.servers = StringUtils.formalizeServers(mapper.servers, " ") { DEFAULT_MONGO_PORT }
    if (mapper.servers.isNullOrBlank()) {
      throw GoblinMongoConfigException("mongo.servers is required")
    }
    mapper.maxSize ?: kotlin.run { mapper.maxSize = 100 }
    mapper.minSize ?: kotlin.run { mapper.minSize = 0 }
    mapper.maxWaitQueueSize ?: kotlin.run { mapper.maxWaitQueueSize = 500 }
    mapper.maxWaitTimeMS ?: kotlin.run { mapper.maxWaitTimeMS = 120000 }
    mapper.maxConnectionLifeTimeMS ?: kotlin.run { mapper.maxConnectionLifeTimeMS = 0 }
    mapper.maxConnectionIdleTimeMS ?: kotlin.run { mapper.maxConnectionIdleTimeMS = 0 }
    mapper.maintenanceInitialDelayMS ?: kotlin.run { mapper.maintenanceInitialDelayMS = 0 }
    mapper.maintenanceFrequencyMS ?: kotlin.run { mapper.maintenanceFrequencyMS = 60000 }
    mapper.autoInit ?: kotlin.run { mapper.autoInit = false }
  }
}