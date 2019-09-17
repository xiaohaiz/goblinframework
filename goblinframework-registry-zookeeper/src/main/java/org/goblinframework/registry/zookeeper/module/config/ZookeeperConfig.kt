package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.config.Config
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.serialization.SerializerMode

@GoblinManagedBean("REGISTRY.ZOOKEEPER")
class ZookeeperConfig
internal constructor(private val mapper: ZookeeperConfigMapper)
  : GoblinManagedObject(), ZookeeperConfigMXBean, Config {

  override fun getName(): String {
    return mapper.name!!
  }

  override fun getServers(): String {
    return mapper.servers!!
  }

  override fun getSerializer(): SerializerMode {
    return mapper.serializer!!
  }

  override fun getCompressor(): CompressorMode? {
    return mapper.compressor
  }

  override fun getCompressionThreshold(): CompressionThreshold? {
    return mapper.compressionThreshold
  }

  override fun destroy() {
    unregisterIfNecessary()
  }
}
