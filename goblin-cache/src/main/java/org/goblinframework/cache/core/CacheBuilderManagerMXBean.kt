package org.goblinframework.cache.core

import java.lang.management.PlatformManagedObject

interface CacheBuilderManagerMXBean : PlatformManagedObject {

  fun getCacheBuilderList(): Array<CacheBuilderMXBean>

}