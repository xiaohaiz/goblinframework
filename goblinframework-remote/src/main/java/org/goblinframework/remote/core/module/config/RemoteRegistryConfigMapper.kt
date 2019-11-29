package org.goblinframework.remote.core.module.config

import org.goblinframework.core.serialization.SerializerMode
import java.io.Serializable

class RemoteRegistryConfigMapper : Serializable {

  var zookeeper: String? = null
  var connectionTimeout: Int? = null
  var sessionTimeout: Int? = null
  var serializer: SerializerMode? = null

  companion object {
    private const val serialVersionUID = -4300434151360859628L
  }
}