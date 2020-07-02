package org.goblinframework.embedded.server

import org.goblinframework.api.annotation.HashSafe

@HashSafe
data class EmbeddedServerId(val mode: EmbeddedServerMode, val name: String) {

  fun asText(): String {
    return "$mode:$name"
  }
}