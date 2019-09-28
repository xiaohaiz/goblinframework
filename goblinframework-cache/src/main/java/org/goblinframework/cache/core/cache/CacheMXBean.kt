package org.goblinframework.cache.core.cache

import org.goblinframework.cache.core.CacheSystem
import java.lang.management.PlatformManagedObject

interface CacheMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

  fun getCacheName(): String

}