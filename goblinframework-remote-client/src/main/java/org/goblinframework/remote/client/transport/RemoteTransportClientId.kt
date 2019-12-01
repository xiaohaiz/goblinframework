package org.goblinframework.remote.client.transport

import org.goblinframework.api.annotation.HashSafe

@HashSafe
data class RemoteTransportClientId(val serverId: String,
                                   val serverHost: String,
                                   val serverPort: Int)