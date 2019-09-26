package org.goblinframework.cache.core.cache

import org.goblinframework.api.cache.CacheSystem
import java.lang.management.PlatformManagedObject

interface CacheMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

  fun getCacheName(): String

}