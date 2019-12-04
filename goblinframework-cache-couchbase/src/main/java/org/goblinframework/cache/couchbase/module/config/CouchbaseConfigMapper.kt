package org.goblinframework.cache.couchbase.module.config

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import java.io.Serializable

class CouchbaseConfigMapper : Serializable {

  var name: String? = null
  var servers: String? = null
  var bucketName: String? = null
  var bucketPassword: String? = null
  var openBucketTimeoutInSeconds: Int? = null
  var serializer: SerializerMode? = null
  var compressionThreshold: CompressionThreshold? = null
  var flushable: Boolean? = null

  companion object {
    private const val serialVersionUID = -9027752370184497434L
  }
}