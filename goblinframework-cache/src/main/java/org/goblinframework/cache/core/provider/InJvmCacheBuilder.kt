package org.goblinframework.cache.core.provider

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.cache.core.Cache
import org.goblinframework.cache.core.CacheBuilder
import org.goblinframework.cache.core.CacheSystem

@Singleton
class InJvmCacheBuilder private constructor() : CacheBuilder {

  companion object {
    @JvmField val INSTANCE = InJvmCacheBuilder()
  }

  override fun system(): CacheSystem {
    return CacheSystem.JVM
  }

  override fun cache(name: String): Cache {
    return InJvmCache(name)
  }
}