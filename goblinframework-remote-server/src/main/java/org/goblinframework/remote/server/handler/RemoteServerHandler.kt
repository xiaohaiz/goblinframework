package org.goblinframework.remote.server.handler

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.event.EventBus
import org.goblinframework.transport.server.handler.TransportRequestContext
import org.goblinframework.transport.server.handler.TransportRequestHandler

@Singleton
class RemoteServerHandler private constructor() : TransportRequestHandler {

  companion object {
    @JvmField val INSTANCE = RemoteServerHandler()
  }

  override fun handleTransportRequest(ctx: TransportRequestContext) {
    EventBus.publish(RemoteServerEvent(ctx))
  }
}