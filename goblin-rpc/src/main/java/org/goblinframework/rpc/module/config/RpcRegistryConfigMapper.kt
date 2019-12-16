package org.goblinframework.rpc.module.config

import org.goblinframework.api.core.SerializerMode
import java.io.Serializable

class RpcRegistryConfigMapper : Serializable {

  var zookeeper: String? = null
  var connectionTimeout: Int? = null
  var sessionTimeout: Int? = null
  var serializer: SerializerMode? = null

  companion object {
    private const val serialVersionUID = -4300434151360859628L
  }
}