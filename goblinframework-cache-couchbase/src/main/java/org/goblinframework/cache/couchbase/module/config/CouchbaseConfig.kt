package org.goblinframework.cache.couchbase.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("CacheCouchbase")
class CouchbaseConfig internal constructor(val mapper: CouchbaseConfigMapper)
  : GoblinManagedObject(), GoblinConfig, CouchbaseConfigMXBean {

  override fun getName(): String {
    return mapper.name!!
  }

  override fun getServers(): String {
    return mapper.servers!!
  }

  override fun getBucketName(): String {
    return mapper.bucketName!!
  }

  override fun getBucketPassword(): String? {
    throw UnsupportedOperationException()
  }

  override fun getOpenBucketTimeoutInSeconds(): Int {
    return mapper.openBucketTimeoutInSeconds!!
  }

  override fun getSerializer(): Serializer {
    return mapper.serializer!!
  }

  override fun getCompressionThreshold(): CompressionThreshold {
    return mapper.compressionThreshold!!
  }

  override fun getFlushable(): Boolean {
    return mapper.flushable!!
  }
}