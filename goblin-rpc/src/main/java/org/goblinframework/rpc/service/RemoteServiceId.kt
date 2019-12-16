package org.goblinframework.rpc.service

import org.goblinframework.api.annotation.HashSafe

@HashSafe
data class RemoteServiceId(val serviceInterface: String, val serviceVersion: String) {

  fun asText(): String {
    return "$serviceInterface/$serviceVersion"
  }

}