package org.goblinframework.cache.core.cache

import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheSystem
import org.goblinframework.api.core.Disposable
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject

@GoblinManagedBean(type = "Cache")
internal class CacheImpl
internal constructor(private val delegator: Cache)
  : GoblinManagedObject(), CacheMXBean, Cache by delegator {

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
    logger.debug("Cache [${cacheSystem()}/${cacheName()}] disposed")
  }

  override fun getCacheSystem(): CacheSystem {
    return delegator.cacheSystem()
  }

  override fun getCacheName(): String {
    return delegator.cacheName()
  }
}