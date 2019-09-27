package org.goblinframework.database.mongo.module.config

import java.lang.management.PlatformManagedObject

interface MongoConfigMXBean : PlatformManagedObject {

  fun getName(): String

}