package org.goblinframework.remote.server.module.config

import org.goblinframework.api.core.CompressorMode
import org.goblinframework.core.compression.CompressionThreshold
import java.io.Serializable

class RemoteServerConfigMapper : Serializable {

  var name: String? = null
  var weight: Int? = null
  var host: String? = null
  var port: Int? = null
  var bossThreads: Int? = null
  var workerThreads: Int? = null
  var compressor: CompressorMode? = null
  var compressionThreshold: CompressionThreshold? = null
  var requestBufferSize: Int? = null
  var requestBufferWorker: Int? = null
  var requestConcurrent: Int? = null
  var responseBufferSize: Int? = null
  var responseBufferWorker: Int? = null
  var maxTimeout: Int? = null

  companion object {
    private const val serialVersionUID = -8785013343998191906L
  }
}