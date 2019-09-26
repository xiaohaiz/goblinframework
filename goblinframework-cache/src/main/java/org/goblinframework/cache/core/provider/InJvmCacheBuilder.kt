package org.goblinframework.cache.core.provider

import org.goblinframework.api.cache.Cache
import org.goblinframework.api.cache.CacheBuilder
import org.goblinframework.api.common.Singleton

@Singleton
class InJvmCacheBuilder private constructor() : CacheBuilder {

  companion object {
    @JvmField val INSTANCE = InJvmCacheBuilder()
  }

  override fun cache(name: String): Cache {
    return InJvmCache(name)
  }
}