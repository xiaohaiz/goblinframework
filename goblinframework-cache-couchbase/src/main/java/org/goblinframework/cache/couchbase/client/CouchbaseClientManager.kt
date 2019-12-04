package org.goblinframework.cache.couchbase.client

import org.apache.commons.lang3.mutable.MutableObject
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.cache.couchbase.module.config.CouchbaseConfigManager
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@ThreadSafe
@GoblinManagedBean("CacheCouchbase")
class CouchbaseClientManager private constructor()
  : GoblinManagedObject(), CouchbaseClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CouchbaseClientManager()
  }

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<String, MutableObject<CouchbaseClient?>>()

  fun getCouchbaseClient(name: String): CouchbaseClient? {
    buffer[name]?.run { return this.value }
    lock.withLock {
      buffer[name]?.run { return this.value }
      val mutable = MutableObject<CouchbaseClient?>()
      CouchbaseConfigManager.INSTANCE.getCouchbaseConfig(name)?.run {
        mutable.value = CouchbaseClient(this)
      }
      buffer[name] = mutable
      return mutable.value
    }
  }

  override fun dispose() {
    lock.withLock {
      buffer.values.forEach { it.value?.dispose() }
      buffer.clear()
    }
  }
}