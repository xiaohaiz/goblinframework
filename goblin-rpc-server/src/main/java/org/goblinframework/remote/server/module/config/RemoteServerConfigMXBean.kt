package org.goblinframework.remote.server.module.config

import org.goblinframework.api.core.CompressorMode
import org.goblinframework.core.compression.CompressionThreshold
import java.lang.management.PlatformManagedObject

interface RemoteServerConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getHost(): String

  fun getPort(): Int

  fun getBossThreads(): Int

  fun getWorkerThreads(): Int

  fun getCompressor(): CompressorMode?

  fun getCompressionThreshold(): CompressionThreshold

  fun getMaxTimeout(): Int

  fun getWeight(): Int

  fun getRequestBufferSize(): Int

  fun getRequestBufferWorker(): Int

  fun getRequestConcurrent(): Int

  fun getResponseBufferSize(): Int

  fun getResponseBufferWorker(): Int
}