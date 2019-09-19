package org.goblinframework.cache.core.provider

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.core.cache.CacheSystem
import org.goblinframework.cache.core.cache.GoblinCache
import org.goblinframework.cache.core.cache.GoblinCacheBuilder

@Install
class InJvmCacheBuilder : GoblinCacheBuilder {

  override fun decorateCacheName(name: String): String {
    return "JVM"
  }

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.JVM
  }

  override fun getCache(name: String): GoblinCache? {
    return InJvmCache.INSTANCE
  }
}