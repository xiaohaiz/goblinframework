package org.goblinframework.cache.core.cache

import org.goblinframework.cache.core.annotation.CacheSystem
import java.lang.management.PlatformManagedObject

interface GoblinCacheMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

  fun getCacheName(): String

}