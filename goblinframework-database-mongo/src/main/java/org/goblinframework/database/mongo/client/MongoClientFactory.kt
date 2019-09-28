package org.goblinframework.database.mongo.client

import com.mongodb.MongoClientSettings
import com.mongodb.ServerAddress
import com.mongodb.async.client.MongoClients
import com.mongodb.connection.netty.NettyStreamFactoryFactory
import org.goblinframework.core.util.StringUtils
import org.goblinframework.database.mongo.module.config.MongoConfig
import java.util.concurrent.TimeUnit

object MongoClientFactory {

  fun build(config: MongoConfig) {
    val addresses = StringUtils.split(config.getServers(), " ")
        .map { ServerAddress(it) }.toList()
    val builder = MongoClientSettings.builder()
        .applyToClusterSettings {
          it.hosts(addresses)
        }
        .applyToConnectionPoolSettings {
          it.maxSize(config.getMaxSize())
          it.minSize(config.getMinSize())
          it.maxWaitQueueSize(config.getMaxWaitQueueSize())
          it.maxWaitTime(config.getMaxWaitTimeMS(), TimeUnit.MILLISECONDS)
          it.maxConnectionLifeTime(config.getMaxConnectionLifeTimeMS(), TimeUnit.MILLISECONDS)
          it.maxConnectionIdleTime(config.getMaxConnectionIdleTimeMS(), TimeUnit.MILLISECONDS)
          it.maintenanceInitialDelay(config.getMaintenanceInitialDelayMS(), TimeUnit.MILLISECONDS)
          it.maintenanceFrequency(config.getMaintenanceFrequencyMS(), TimeUnit.MILLISECONDS)
        }
    if (config.getStream() == "netty") {
      builder.streamFactoryFactory(NettyStreamFactoryFactory.builder().build())
    }
    val settings = builder.build()
    val client = MongoClients.create(settings)

    println("")

  }
}