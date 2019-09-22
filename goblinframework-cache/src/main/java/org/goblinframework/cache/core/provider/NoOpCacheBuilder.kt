package org.goblinframework.cache.core.provider

import org.goblinframework.core.cache.GoblinCache
import org.goblinframework.core.cache.GoblinCacheBuilder

class NoOpCacheBuilder private constructor() : GoblinCacheBuilder {

  companion object {
    @JvmField val INSTANCE = NoOpCacheBuilder()
  }

  override fun decorateCacheName(name: String): String {
    return "NOP"
  }

  override fun getCache(name: String): GoblinCache? {
    return NoOpCache.INSTANCE
  }
}