package org.goblinframework.cache.core.cache

import java.lang.management.PlatformManagedObject

interface GoblinCacheMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

  fun getCacheName(): String

}