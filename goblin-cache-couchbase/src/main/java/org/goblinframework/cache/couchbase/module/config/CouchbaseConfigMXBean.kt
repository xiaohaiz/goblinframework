package org.goblinframework.cache.couchbase.module.config

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import java.lang.management.PlatformManagedObject

interface CouchbaseConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getServers(): String

  fun getBucketName(): String

  fun getBucketPassword(): String?

  fun getOpenBucketTimeoutInSeconds(): Int

  fun getSerializer(): SerializerMode

  fun getCompressionThreshold(): CompressionThreshold

  fun getFlushable(): Boolean

}