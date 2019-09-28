package org.goblinframework.transport.client.flight

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.transport.client.handler.TransportResponseContext
import org.goblinframework.transport.client.handler.TransportResponseHandler

@Singleton
class MessageFlightResponseHandler private constructor() : TransportResponseHandler {

  companion object {
    @JvmField val INSTANCE = MessageFlightResponseHandler()
  }

  override fun handleTransportResponse(ctx: TransportResponseContext) {
    val response = ctx.response
    MessageFlightManager.INSTANCE.onResponse(response)
  }

}
