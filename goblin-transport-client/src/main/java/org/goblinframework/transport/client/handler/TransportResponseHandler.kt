package org.goblinframework.transport.client.handler

@FunctionalInterface
interface TransportResponseHandler {

  fun handleTransportResponse(ctx: TransportResponseContext)

}