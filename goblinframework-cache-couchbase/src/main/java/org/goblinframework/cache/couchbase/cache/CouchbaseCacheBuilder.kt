package org.goblinframework.cache.couchbase.cache

import org.apache.commons.lang3.mutable.MutableObject
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.cache.core.Cache
import org.goblinframework.cache.core.CacheBuilder
import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.cache.core.cache.CacheBuilderMXBean
import org.goblinframework.cache.core.cache.CacheBuilderManager
import org.goblinframework.cache.core.cache.CacheMXBean
import org.goblinframework.cache.couchbase.client.CouchbaseClientManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@ThreadSafe
@GoblinManagedBean("CacheCouchbase")
class CouchbaseCacheBuilder private constructor()
  : GoblinManagedObject(), CacheBuilder, CacheBuilderMXBean {

  companion object {
    val INSTANCE = CouchbaseCacheBuilder()
  }

  private val buffer = ConcurrentHashMap<String, MutableObject<CouchbaseCache?>>()
  private val lock = ReentrantLock()

  override fun initializeBean() {
    CacheBuilderManager.INSTANCE.registerCacheBuilder(this)
  }

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.CBS
  }

  override fun getCache(name: String): Cache? {
    buffer[name]?.run { return this.value }
    lock.withLock {
      buffer[name]?.run { return this.value }
      val mutable = MutableObject<CouchbaseCache?>()
      CouchbaseClientManager.INSTANCE.getCouchbaseClient(name)?.run {
        mutable.value = CouchbaseCache(this)
      }
      buffer[name] = mutable
      return mutable.value
    }
  }

  override fun getCacheList(): Array<CacheMXBean> {
    return buffer.values.mapNotNull { it.value }
        .sortedBy { it.cacheName }.toTypedArray()
  }

  override fun disposeBean() {
    CacheBuilderManager.INSTANCE.unregisterCacheBuilder(this)
    lock.withLock {
      buffer.values.mapNotNull { it.value }.forEach { it.dispose() }
      buffer.clear()
    }
  }
}