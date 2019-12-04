package org.goblinframework.cache.couchbase.client

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("CacheCouchbase")
class CouchbaseClient internal constructor()
  : GoblinManagedObject(), CouchbaseClientMXBean {
}