package org.goblinframework.remote.client.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.serialization.SerializerMode
import java.lang.management.PlatformManagedObject

interface RemoteClientConfigMXBean : PlatformManagedObject {

  fun getSerializer(): SerializerMode

  fun getCompressor(): CompressorMode?

  fun getCompressionThreshold(): CompressionThreshold

}