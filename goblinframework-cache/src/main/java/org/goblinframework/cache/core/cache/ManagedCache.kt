package org.goblinframework.cache.core.cache

import org.goblinframework.api.cache.Cache
import org.goblinframework.api.common.Disposable
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "cache")
internal class ManagedCache
internal constructor(private val delegator: Cache)
  : GoblinManagedObject(), CacheMXBean, Cache by delegator {

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
    logger.debug("Cache [${cacheSystem()}/${cacheName()}] disposed")
  }
}