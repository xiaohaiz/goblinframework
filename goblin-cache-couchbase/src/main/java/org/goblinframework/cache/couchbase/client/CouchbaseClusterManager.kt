package org.goblinframework.cache.couchbase.client

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean("CacheCouchbase")
class CouchbaseClusterManager private constructor()
  : GoblinManagedObject(), CouchbaseClusterManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CouchbaseClusterManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<String, CouchbaseCluster>()

  fun getCouchbaseCluster(servers: String): com.couchbase.client.java.CouchbaseCluster {
    val couchbaseCluster = lock.read { buffer[servers] } ?: lock.write {
      buffer[servers] ?: kotlin.run {
        val cluster = CouchbaseCluster(servers)
        buffer[servers] = cluster
        cluster
      }
    }
    couchbaseCluster.retain()
    return couchbaseCluster.cluster
  }

  fun closeCouchbaseCluster(servers: String) {
    lock.write {
      val couchbaseCluster = buffer[servers] ?: return
      if (couchbaseCluster.release()) {
        buffer.remove(servers)
        couchbaseCluster.dispose()
      }
    }
  }

  override fun disposeBean() {
    lock.write {
      buffer.values.forEach { it.dispose() }
      buffer.clear()
    }
  }
}