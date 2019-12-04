package org.goblinframework.cache.couchbase.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.serialization.Serializer
import java.lang.management.PlatformManagedObject

interface CouchbaseConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getServers(): String

  fun getBucketName(): String

  fun getBucketPassword(): String?

  fun getOpenBucketTimeoutInSeconds(): Int

  fun getSerializer(): Serializer

  fun getCompressionThreshold(): CompressionThreshold

  fun getFlushable(): Boolean

}