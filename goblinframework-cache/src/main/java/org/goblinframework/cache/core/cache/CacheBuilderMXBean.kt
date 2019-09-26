package org.goblinframework.cache.core.cache

import org.goblinframework.api.cache.CacheSystem
import java.lang.management.PlatformManagedObject

interface CacheBuilderMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

  fun getCacheList(): Array<CacheMXBean>

}