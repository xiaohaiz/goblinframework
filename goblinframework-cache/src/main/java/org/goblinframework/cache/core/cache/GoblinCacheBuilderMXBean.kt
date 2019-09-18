package org.goblinframework.cache.core.cache

import java.lang.management.PlatformManagedObject

interface GoblinCacheBuilderMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

}