package org.goblinframework.transport.server.handler

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.bootstrap.GoblinSystem
import org.goblinframework.transport.core.protocol.HandshakeRequest

@Singleton
class DefaultHandshakeRequestHandler private constructor() : HandshakeRequestHandler {

  companion object {
    @JvmField val INSTANCE = DefaultHandshakeRequestHandler()
  }

  override fun handleHandshakeRequest(request: HandshakeRequest): Boolean {
    return request.serverId == null || request.serverId == GoblinSystem.getApplicationId()
  }
}