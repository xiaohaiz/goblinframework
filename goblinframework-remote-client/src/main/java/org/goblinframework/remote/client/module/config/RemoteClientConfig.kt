package org.goblinframework.remote.client.module.config

import org.goblinframework.api.core.CompressorMode
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("RemoteClient")
class RemoteClientConfig
internal constructor(internal val mapper: RemoteClientConfigMapper)
  : GoblinManagedObject(), GoblinConfig, RemoteClientConfigMXBean {

  override fun getWorkerThreads(): Int {
    return mapper.workerThreads!!
  }

  override fun getSendHeartbeat(): Boolean {
    return mapper.sendHeartbeat!!
  }

  override fun getSerializer(): SerializerMode {
    return mapper.serializer!!
  }

  override fun getCompressor(): CompressorMode? {
    return mapper.compressor
  }

  override fun getCompressionThreshold(): CompressionThreshold {
    return mapper.compressionThreshold!!
  }

  override fun getMaxTimeout(): Int {
    return mapper.maxTimeout!!
  }

  override fun getRequestBufferSize(): Int {
    return mapper.requestBufferSize!!
  }

  override fun getRequestBufferWorker(): Int {
    return mapper.requestBufferWorker!!
  }

  override fun getRequestConcurrent(): Int {
    return mapper.requestConcurrent!!
  }

  override fun getResponseBufferSize(): Int {
    return mapper.responseBufferSize!!
  }

  override fun getResponseBufferWorker(): Int {
    return mapper.responseBufferWorker!!
  }
}