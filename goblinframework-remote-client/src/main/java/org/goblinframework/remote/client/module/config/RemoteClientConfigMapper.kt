package org.goblinframework.remote.client.module.config

import org.goblinframework.api.core.CompressorMode
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import java.io.Serializable

class RemoteClientConfigMapper : Serializable {

  var workerThreads: Int? = null
  var sendHeartbeat: Boolean? = null
  var maxTimeout: Int? = null
  var serializer: SerializerMode? = null
  var compressor: CompressorMode? = null
  var compressionThreshold: CompressionThreshold? = null
  var requestBufferSize: Int? = null
  var requestBufferWorker: Int? = null
  var requestConcurrent: Int? = null
  var responseBufferSize: Int? = null
  var responseBufferWorker: Int? = null

  companion object {
    private const val serialVersionUID = 2349386765518738752L
  }
}