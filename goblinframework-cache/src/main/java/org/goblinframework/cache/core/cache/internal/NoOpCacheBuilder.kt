package org.goblinframework.cache.core.cache.internal

import org.goblinframework.cache.core.Cache
import org.goblinframework.cache.core.CacheBuilder
import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.cache.core.cache.CacheBuilderMXBean
import org.goblinframework.cache.core.cache.CacheMXBean
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("Cache")
class NoOpCacheBuilder : GoblinManagedObject(), CacheBuilder, CacheBuilderMXBean {

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.NOP
  }

  override fun getCache(name: String): Cache {
    return NoOpCache.INSTANCE
  }

  override fun getCacheList(): Array<CacheMXBean> {
    return arrayOf(NoOpCache.INSTANCE)
  }
}