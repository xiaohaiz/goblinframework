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

  override fun getSerializer(): SerializerMode {
    return mapper.serializer!!
  }

  override fun getCompressor(): CompressorMode? {
    return mapper.compressor
  }

  override fun getCompressionThreshold(): CompressionThreshold {
    return mapper.compressionThreshold!!
  }

}