package org.goblinframework.embedded.server

import org.goblinframework.api.core.Lifecycle

interface EmbeddedServer : Lifecycle {

  fun id(): EmbeddedServerId

  fun getHost(): String?

  fun getPort(): Int?

}