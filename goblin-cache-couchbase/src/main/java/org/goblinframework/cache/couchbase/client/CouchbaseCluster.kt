package org.goblinframework.cache.couchbase.client

import org.goblinframework.api.core.ReferenceCount
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.GoblinReferenceCount

@GoblinManagedBean("CacheCouchbase")
class CouchbaseCluster internal constructor(private val servers: String)
  : GoblinManagedObject(), ReferenceCount, CouchbaseClusterMXBean {

  private val referenceCount = GoblinReferenceCount()
  internal val cluster: com.couchbase.client.java.CouchbaseCluster

  init {
    val nodeList = servers.split(" ")
    val environment = CouchbaseEnvironmentManager.INSTANCE.getCouchbaseEnvironment()
    this.cluster = com.couchbase.client.java.CouchbaseCluster.create(environment, nodeList)
  }

  override fun count(): Int {
    return referenceCount.count()
  }

  override fun retain() {
    referenceCount.retain()
  }

  override fun retain(increment: Int) {
    referenceCount.retain(increment)
  }

  override fun release(): Boolean {
    return referenceCount.release()
  }

  override fun release(decrement: Int): Boolean {
    return referenceCount.release(decrement)
  }

  override fun getServers(): String {
    return servers
  }

  override fun disposeBean() {
    val state = if (cluster.disconnect()) "SUCCESS" else "FAILURE"
    logger.debug("{Couchbase} Couchbase cluster [$servers] disconnected [$state]")
  }
}