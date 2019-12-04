package org.goblinframework.cache.core.cache

import java.lang.management.PlatformManagedObject

interface CacheMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

  fun getCacheName(): String

}