package org.goblinframework.remote.server.transport

import org.goblinframework.remote.server.dispatcher.request.RemoteServerRequestDispatcher
import org.goblinframework.transport.server.handler.TransportRequestContext
import org.goblinframework.transport.server.handler.TransportRequestHandler

class RemoteTransportServerHandler internal constructor() : TransportRequestHandler {

  override fun handleTransportRequest(ctx: TransportRequestContext) {
    RemoteServerRequestDispatcher.INSTANCE.onRequest(ctx)
  }
}