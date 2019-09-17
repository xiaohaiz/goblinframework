package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.core.compression.CompressionThreshold
import org.goblinframework.core.compression.CompressorMode
import org.goblinframework.core.serialization.SerializerMode

import java.io.Serializable

class ZookeeperConfigMapper : Serializable {

  var name: String? = null
  var servers: String? = null
  var serializer: SerializerMode? = null
  var compressor: CompressorMode? = null
  var compressionThreshold: CompressionThreshold? = null

  companion object {
    private const val serialVersionUID = -7052803223494771305L
  }

}
