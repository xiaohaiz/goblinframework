package org.goblinframework.cache.core.cache

import org.goblinframework.api.common.Disposable
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject

@GoblinManagedBean("CACHE")
internal class GoblinCacheImpl
internal constructor(private val delegator: GoblinCache)
  : GoblinManagedObject(), Disposable, GoblinCacheMXBean, GoblinCache by delegator {

  override fun getCacheSystem(): CacheSystem {
    return delegator.cacheSystem
  }

  override fun getCacheName(): String {
    return delegator.name
  }

}