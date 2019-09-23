package org.goblinframework.cache.core.provider

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheBuilder

@Singleton
class InJvmCacheBuilder private constructor() : CacheBuilder {

  companion object {
    @JvmField val INSTANCE = InJvmCacheBuilder()
  }

  override fun decorateCacheName(name: String): String {
    return "\$JVM"
  }

  override fun cache(name: String): Cache {
    return InJvmCache.INSTANCE
  }
}