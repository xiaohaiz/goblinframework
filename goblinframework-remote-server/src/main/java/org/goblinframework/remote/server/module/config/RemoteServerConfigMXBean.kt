package org.goblinframework.remote.server.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.compression.CompressorMode
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