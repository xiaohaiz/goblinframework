package org.goblinframework.transport.server.handler

interface TransportRequestHandler {

  fun handleTransportRequest(ctx: TransportRequestContext)

}