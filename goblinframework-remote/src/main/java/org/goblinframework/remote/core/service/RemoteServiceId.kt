package org.goblinframework.remote.core.service

import org.goblinframework.api.common.HashSafe

@HashSafe
data class RemoteServiceId(val interfaceClass: Class<*>,
                           val group: String,
                           val version: String)