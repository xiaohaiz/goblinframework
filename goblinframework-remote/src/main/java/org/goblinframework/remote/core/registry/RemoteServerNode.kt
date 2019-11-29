package org.goblinframework.remote.core.registry

import org.goblinframework.core.util.HttpUtils

class RemoteServerNode {

  var serverId: String? = null
  var serverName: String? = null
  var serverHost: String? = null
  var serverPort: Int? = null
  var serverVersion: String? = null
  var serverDomain: String? = null
  var serverWeight: Int? = null

  fun toNode(): String {
    val map = LinkedHashMap<String, Any>()
    map["serverId"] = serverId!!
    map["serverName"] = serverName!!
    map["serverHost"] = serverHost!!
    map["serverPort"] = serverPort!!
    map["serverVersion"] = serverVersion!!
    map["serverDomain"] = serverDomain!!
    map["serverWeight"] = serverWeight!!
    return HttpUtils.buildQueryString(map)
  }

  override fun toString(): String {
    return toNode()
  }
}