package org.goblinframework.transport.client.handler

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.system.GoblinSystem
import org.goblinframework.transport.core.protocol.ShutdownRequest

@Singleton
class DefaultShutdownRequestHandler private constructor() : ShutdownRequestHandler {

  companion object {
    @JvmField val INSTANCE = DefaultShutdownRequestHandler()
  }

  override fun handleShutdownRequest(request: ShutdownRequest): Boolean {
    return request.clientId == null || request.clientId == GoblinSystem.applicationId()
  }
}