package org.goblinframework.cache.core.provider

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.core.annotation.CacheSystem
import org.goblinframework.cache.core.cache.GoblinCache
import org.goblinframework.cache.core.cache.GoblinCacheBuilder

@Install
class NoOpCacheBuilder : GoblinCacheBuilder {

  override fun decorateCacheName(name: String): String {
    return "NOP"
  }

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.NOP
  }

  override fun getCache(name: String): GoblinCache? {
    return NoOpCache.INSTANCE
  }
}