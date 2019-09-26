package org.goblinframework.remote.client.connection

import org.goblinframework.api.common.HashSafe

@HashSafe
data class RemoteConnectionId(val serverId: String,
                              val serverHost: String,
                              val serverPort: Int) {

  fun name(): String {
    return "$serverId/$serverHost/$serverPort"
  }
}