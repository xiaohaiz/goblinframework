package org.goblinframework.cache.core

import java.lang.management.PlatformManagedObject

interface CacheMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

  fun getCacheName(): String

}