package org.goblinframework.transport.client.handler

import org.goblinframework.transport.core.protocol.ShutdownRequest

@FunctionalInterface
interface ShutdownRequestHandler {

  fun handleShutdownRequest(request: ShutdownRequest): Boolean

}