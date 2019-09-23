package org.goblinframework.transport.server.handler

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.system.GoblinSystem
import org.goblinframework.transport.core.protocol.HandshakeRequest

@Singleton
class DefaultHandshakeRequestHandler private constructor() : HandshakeRequestHandler {

  companion object {
    @JvmField val INSTANCE = DefaultHandshakeRequestHandler()
  }

  override fun handleHandshakeRequest(request: HandshakeRequest): Boolean {
    return request.serverId == null || request.serverId == GoblinSystem.applicationId()
  }
}