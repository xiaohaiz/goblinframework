package org.goblinframework.cache.core.cache

import org.goblinframework.api.cache.GoblinCacheSystem
import java.lang.management.PlatformManagedObject

interface GoblinCacheMXBean : PlatformManagedObject {

  fun getCacheSystem(): GoblinCacheSystem

  fun getCacheName(): String

}