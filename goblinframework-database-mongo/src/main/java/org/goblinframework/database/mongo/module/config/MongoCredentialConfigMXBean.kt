package org.goblinframework.database.mongo.module.config

import java.lang.management.PlatformManagedObject

interface MongoCredentialConfigMXBean : PlatformManagedObject {

  fun getDatabase(): String?

  fun getUsername(): String?

  fun getPassword(): String?

}