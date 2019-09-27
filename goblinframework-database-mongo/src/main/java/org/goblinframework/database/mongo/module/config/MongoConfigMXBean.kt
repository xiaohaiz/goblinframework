package org.goblinframework.database.mongo.module.config

import java.lang.management.PlatformManagedObject

interface MongoConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getServers(): String

  fun getStream(): String

  fun getMaxSize(): Int

  fun getMinSize(): Int

  fun getMaxWaitQueueSize(): Int

  fun getMaxWaitTimeMS(): Long

  fun getMaxConnectionLifeTimeMS(): Long

  fun getMaxConnectionIdleTimeMS(): Long

  fun getMaintenanceInitialDelayMS(): Long

  fun getMaintenanceFrequencyMS(): Long

  fun getCredentialConfigList(): Array<MongoCredentialConfigMXBean>
}