package org.goblinframework.cache.core.provider

import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.GoblinCacheBuilder

class NoOpCacheBuilder private constructor() : GoblinCacheBuilder {

  companion object {
    @JvmField val INSTANCE = NoOpCacheBuilder()
  }

  override fun decorateCacheName(name: String): String {
    return "NOP"
  }

  override fun getCache(name: String): Cache? {
    return NoOpCache.INSTANCE
  }
}