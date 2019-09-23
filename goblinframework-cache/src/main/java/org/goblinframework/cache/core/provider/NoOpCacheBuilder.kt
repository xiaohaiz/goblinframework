package org.goblinframework.cache.core.provider

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheBuilder

@Singleton
class NoOpCacheBuilder private constructor() : CacheBuilder {

  companion object {
    @JvmField val INSTANCE = NoOpCacheBuilder()
  }

  override fun decorateCacheName(name: String): String {
    return "\$NOP"
  }

  override fun cache(name: String): Cache {
    return NoOpCache.INSTANCE
  }
}