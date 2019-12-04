package org.goblinframework.cache.couchbase.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.serialization.Serializer
import java.io.Serializable

class CouchbaseConfigMapper : Serializable {

  var name: String? = null
  var servers: String? = null
  var bucketName: String? = null
  var bucketPassword: String? = null
  var openBucketTimeoutInSeconds: Int? = null
  var serializer: Serializer? = null
  var compressionThreshold: CompressionThreshold? = null
  var flushable: Boolean? = null

  companion object {
    private const val serialVersionUID = -9027752370184497434L
  }
}