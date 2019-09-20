package org.goblinframework.cache.core.cache

import org.goblinframework.cache.core.annotation.CacheSystem
import java.lang.management.PlatformManagedObject

interface GoblinCacheBuilderMXBean : PlatformManagedObject {

  fun getCacheSystem(): CacheSystem

}