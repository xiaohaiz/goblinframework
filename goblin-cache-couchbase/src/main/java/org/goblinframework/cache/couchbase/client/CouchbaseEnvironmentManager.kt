package org.goblinframework.cache.couchbase.client

import com.couchbase.client.java.env.CouchbaseEnvironment
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.atomic.AtomicReference

@Singleton
@ThreadSafe
@GoblinManagedBean("CacheCouchbase")
class CouchbaseEnvironmentManager private constructor()
  : GoblinManagedObject(), CouchbaseEnvironmentManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CouchbaseEnvironmentManager()
  }

  private val environmentReference = AtomicReference<CouchbaseEnvironment?>()

  @Synchronized
  fun getCouchbaseEnvironment(): CouchbaseEnvironment {
    environmentReference.get()?.run {
      return this
    }
    val environment = DefaultCouchbaseEnvironment.builder()
        .dnsSrvEnabled(false)
        .build()
    environmentReference.set(environment)
    return environment
  }

  @Synchronized
  override fun disposeBean() {
    environmentReference.getAndSet(null)?.run {
      val state = if (this.shutdownAsync().toBlocking().single()) "SUCCESS" else "FAILURE"
      logger.debug("{Couchbase} Couchbase environment shutdown [$state]")
    }
  }
}