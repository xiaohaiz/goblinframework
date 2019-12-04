package org.goblinframework.cache.redis.cache

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.cache.core.Cache
import org.goblinframework.cache.core.CacheBuilder
import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.cache.core.cache.CacheBuilderMXBean
import org.goblinframework.cache.core.cache.CacheBuilderManager2
import org.goblinframework.cache.core.cache.CacheMXBean
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@ThreadSafe
@GoblinManagedBean("CacheRedis")
class RedisCacheBuilder2 private constructor()
  : GoblinManagedObject(), CacheBuilder, CacheBuilderMXBean {

  companion object {
    @JvmField val INSTANCE = RedisCacheBuilder2()
  }

  override fun initializeBean() {
    CacheBuilderManager2.INSTANCE.registerCacheBuilder(this)
  }

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.RDS
  }

  override fun getCache(name: String): Cache? {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getCacheList(): Array<CacheMXBean> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun disposeBean() {
    CacheBuilderManager2.INSTANCE.unregisterCacheBuilder(this)
  }
}