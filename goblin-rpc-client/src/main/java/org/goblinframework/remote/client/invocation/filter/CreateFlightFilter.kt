package org.goblinframework.remote.client.invocation.filter

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.function.Block1
import org.goblinframework.remote.client.invocation.RemoteClientInvocation
import org.goblinframework.rpc.filter.RpcFilterChain
import org.goblinframework.transport.client.flight.MessageFlightManager

@Singleton
class CreateFlightFilter private constructor() : AbstractInternalFilter() {

  companion object {
    val INSTANCE = CreateFlightFilter()
  }

  override fun doFilter(invocation: RemoteClientInvocation, chain: RpcFilterChain<RemoteClientInvocation>) {
    val requireResponse = !invocation.noResponseWait()
    invocation.flight = MessageFlightManager.INSTANCE.createMessageFlight(requireResponse)
        .prepareRequest(Block1 {
          it.request().serializer = 0
          it.request().compressor = 0
          it.request().payload = invocation.encodedRequest
          it.request().hasPayload = true
          it.request().rawPayload = true
          it.request().response = requireResponse
        })
    chain.filter(invocation)
  }
}