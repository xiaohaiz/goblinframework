package org.goblinframework.remote.client.module.config

import org.goblinframework.api.core.CompressorMode
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.compression.CompressionThreshold
import java.lang.management.PlatformManagedObject

interface RemoteClientConfigMXBean : PlatformManagedObject {

  fun getSerializer(): SerializerMode

  fun getCompressor(): CompressorMode?

  fun getCompressionThreshold(): CompressionThreshold

}