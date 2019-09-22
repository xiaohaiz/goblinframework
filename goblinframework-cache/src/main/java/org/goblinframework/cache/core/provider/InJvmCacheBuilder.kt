package org.goblinframework.cache.core.provider

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.core.cache.GoblinCache
import org.goblinframework.cache.core.cache.GoblinCacheBuilder
import org.goblinframework.core.cache.GoblinCacheSystem

@Install
class InJvmCacheBuilder : GoblinCacheBuilder {

  override fun decorateCacheName(name: String): String {
    return "JVM"
  }

  override fun getCacheSystem(): GoblinCacheSystem {
    return GoblinCacheSystem.JVM
  }

  override fun getCache(name: String): GoblinCache? {
    return InJvmCache.INSTANCE
  }
}