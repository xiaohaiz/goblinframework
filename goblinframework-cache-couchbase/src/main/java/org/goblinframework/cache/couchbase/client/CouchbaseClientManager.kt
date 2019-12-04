package org.goblinframework.cache.couchbase.client

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@Singleton
@ThreadSafe
@GoblinManagedBean("CacheCouchbase")
class CouchbaseClientManager private constructor()
  : GoblinManagedObject(), CouchbaseClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CouchbaseClientManager()
  }
}