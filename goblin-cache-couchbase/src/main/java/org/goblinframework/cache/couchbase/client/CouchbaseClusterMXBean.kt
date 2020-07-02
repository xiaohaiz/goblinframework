package org.goblinframework.cache.couchbase.client

import java.lang.management.PlatformManagedObject

interface CouchbaseClusterMXBean : PlatformManagedObject {

  fun getServers(): String

}