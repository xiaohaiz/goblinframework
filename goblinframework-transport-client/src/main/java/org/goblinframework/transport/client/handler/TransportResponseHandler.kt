package org.goblinframework.transport.client.handler

interface TransportResponseHandler {

  fun handleTransportResponse(ctx: TransportResponseContext)

}