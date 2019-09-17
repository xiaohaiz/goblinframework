package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.serialization.SerializerMode
import java.lang.management.PlatformManagedObject

interface ZookeeperConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getServers(): String

  fun getSerializer(): SerializerMode

  fun getCompressor(): CompressorMode?

  fun getCompressionThreshold(): CompressionThreshold?

}