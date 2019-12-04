package org.goblinframework.remote.client.module.config

import org.goblinframework.api.core.CompressorMode
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import java.lang.management.PlatformManagedObject

interface RemoteClientConfigMXBean : PlatformManagedObject {

  fun getWorkerThreads(): Int

  fun getSendHeartbeat(): Boolean

  fun getSerializer(): SerializerMode

  fun getCompressor(): CompressorMode?

  fun getCompressionThreshold(): CompressionThreshold

  fun getMaxTimeout(): Int

  fun getRequestBufferSize(): Int

  fun getRequestBufferWorker(): Int

  fun getRequestConcurrent(): Int

  fun getResponseBufferSize(): Int

  fun getResponseBufferWorker(): Int

}