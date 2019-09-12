package org.goblinframework.transport.client.setting

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.bootstrap.GoblinSystem
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