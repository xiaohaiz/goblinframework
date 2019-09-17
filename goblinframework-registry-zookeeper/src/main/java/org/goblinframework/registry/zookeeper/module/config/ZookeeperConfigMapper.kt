package org.goblinframework.registry.zookeeper.module.config

import org.goblinframework.core.serialization.SerializerMode
import java.io.Serializable

class ZookeeperConfigMapper : Serializable {

  var name: String? = null
  var servers: String? = null
  var serializer: SerializerMode? = null

  companion object {
    private const val serialVersionUID = -7052803223494771305L
  }

}
