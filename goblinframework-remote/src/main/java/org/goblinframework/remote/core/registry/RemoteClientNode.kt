package org.goblinframework.remote.core.registry

import org.goblinframework.core.util.HttpUtils

class RemoteClientNode {

  var clientId: String? = null
  var clientName: String? = null
  var clientHost: String? = null
  var clientPid: Int? = null

  fun toNode(): String {
    val map = LinkedHashMap<String, Any>()
    map["clientId"] = clientId!!
    map["clientName"] = clientName!!
    map["clientHost"] = clientHost!!
    map["clientPid"] = clientPid!!
    return HttpUtils.buildQueryString(map)
  }

  override fun toString(): String {
    return toNode()
  }
}