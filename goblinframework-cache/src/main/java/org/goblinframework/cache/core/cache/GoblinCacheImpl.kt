package org.goblinframework.cache.core.cache

import org.goblinframework.api.cache.GoblinCache
import org.goblinframework.api.cache.GoblinCacheSystem
import org.goblinframework.api.common.Disposable
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "cache")
internal class GoblinCacheImpl
internal constructor(private val delegator: GoblinCache)
  : GoblinManagedObject(), Disposable, GoblinCacheMXBean, GoblinCache by delegator {

  override fun getCacheSystem(): GoblinCacheSystem {
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