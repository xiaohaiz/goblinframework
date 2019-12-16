package org.goblinframework.rpc.service

import org.goblinframework.api.annotation.HashSafe

@HashSafe
data class RemoteClientId(val serviceId: RemoteServiceId, val serviceGroup: String)