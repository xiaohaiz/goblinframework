package org.goblinframework.cache.couchbase.cache

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.cache.core.Cache
import org.goblinframework.cache.core.CacheBuilder
import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.cache.couchbase.client.CouchbaseClientManager

@Singleton
class CouchbaseCacheBuilder private constructor() : CacheBuilder {

  companion object {
    @JvmField val INSTANCE = CouchbaseCacheBuilder()
  }

  override fun getCacheSystem(): CacheSystem {
    return CacheSystem.CBS
  }

  override fun getCache(name: String): Cache? {
    val client = CouchbaseClientManager.INSTANCE.getCouchbaseClient(name) ?: return null
    return CouchbaseCache(client)
  }
}