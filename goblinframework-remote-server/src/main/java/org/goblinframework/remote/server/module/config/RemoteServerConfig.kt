package org.goblinframework.remote.server.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.config.GoblinConfig
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("RemoteServer")
class RemoteServerConfig internal constructor(internal val mapper: RemoteServerConfigMapper)
  : GoblinManagedObject(), GoblinConfig, RemoteServerConfigMXBean {

  override fun getName(): String {
    return mapper.name!!
  }

  override fun getHost(): String {
    return mapper.host!!
  }

  override fun getPort(): Int {
    return mapper.port!!
  }

  override fun getBossThreads(): Int {
    return mapper.bossThreads!!
  }

  override fun getWorkerThreads(): Int {
    return mapper.workerThreads!!
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

  override fun getWeight(): Int {
    return mapper.weight!!
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