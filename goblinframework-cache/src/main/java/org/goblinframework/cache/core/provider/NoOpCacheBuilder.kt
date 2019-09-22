package org.goblinframework.cache.core.provider

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.core.cache.GoblinCacheBuilder
import org.goblinframework.core.cache.GoblinCache
import org.goblinframework.core.cache.GoblinCacheSystem

@Install
class NoOpCacheBuilder : GoblinCacheBuilder {

  override fun decorateCacheName(name: String): String {
    return "NOP"
  }

  override fun getCacheSystem(): GoblinCacheSystem {
    return GoblinCacheSystem.NOP
  }

  override fun getCache(name: String): GoblinCache? {
    return NoOpCache.INSTANCE
  }
}