package org.goblinframework.cache.core.cache

import org.goblinframework.core.cache.GoblinCacheSystem
import java.lang.management.PlatformManagedObject

interface GoblinCacheMXBean : PlatformManagedObject {

  fun getCacheSystem(): GoblinCacheSystem

  fun getCacheName(): String

}