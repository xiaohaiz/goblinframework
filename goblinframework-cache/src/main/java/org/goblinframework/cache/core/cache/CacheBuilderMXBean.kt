package org.goblinframework.cache.core.cache

import org.goblinframework.cache.core.CacheSystem
import java.lang.management.PlatformManagedObject

interface CacheBuilderMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

  fun getCacheList(): Array<CacheMXBean>

}