package org.goblinframework.cache.core.cache

import org.goblinframework.core.cache.GoblinCacheSystem
import java.lang.management.PlatformManagedObject

interface GoblinCacheBuilderMXBean : PlatformManagedObject {

  fun getCacheSystem(): GoblinCacheSystem

}