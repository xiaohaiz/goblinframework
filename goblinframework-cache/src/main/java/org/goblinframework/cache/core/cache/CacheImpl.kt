package org.goblinframework.cache.core.cache

import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheSystem
import org.goblinframework.api.common.Disposable
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "cache")
internal class CacheImpl
internal constructor(private val delegator: Cache)
  : GoblinManagedObject(), Disposable, GoblinCacheMXBean, Cache by delegator {

  override fun getCacheSystem(): CacheSystem {
    return delegator.cacheSystem
  }

  override fun getCacheName(): String {
    return delegator.name
  }

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
    logger.debug("GOBLIN cache [$cacheSystem/${getCacheName()}] disposed")
  }
}