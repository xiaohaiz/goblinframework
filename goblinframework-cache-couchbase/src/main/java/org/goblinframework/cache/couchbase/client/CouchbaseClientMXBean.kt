package org.goblinframework.cache.couchbase.client

import com.couchbase.client.java.bucket.BucketType
import java.lang.management.PlatformManagedObject

interface CouchbaseClientMXBean : PlatformManagedObject {

  fun getName(): String

  fun getFlushable(): Boolean

  fun getBucketType(): BucketType

}