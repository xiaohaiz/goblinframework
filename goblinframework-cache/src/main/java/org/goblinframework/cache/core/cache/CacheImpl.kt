package org.goblinframework.cache.core.cache

import org.goblinframework.api.function.Disposable
import org.goblinframework.cache.core.Cache
import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

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