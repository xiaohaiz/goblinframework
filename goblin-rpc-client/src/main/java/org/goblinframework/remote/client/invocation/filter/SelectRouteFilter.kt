package org.goblinframework.remote.client.invocation.filter

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.remote.client.invocation.RemoteClientInvocation
import org.goblinframework.remote.client.module.exception.ClientNoTransportRouteException
import org.goblinframework.rpc.filter.RpcFilterChain
import org.goblinframework.transport.client.channel.TransportClient

@Singleton
class SelectRouteFilter private constructor() : AbstractInternalFilter() {

  companion object {
    @JvmField val INSTANCE = SelectRouteFilter()
  }

  override fun doFilter(invocation: RemoteClientInvocation, chain: RpcFilterChain<RemoteClientInvocation>) {
    val routes = mutableListOf<TransportClient>()
    if (invocation.dispatchAll()) {
      routes.addAll(invocation.client.selectTransportClients())
    } else {
      invocation.client.selectTransportClient()?.run {
        routes.add(this)
      }
    }
    if (routes.isEmpty()) {
      if (invocation.ignoreNoProvider()) {
        invocation.complete(null)
      } else {
        logger.error("{CLIENT_NO_TRANSPORT_ERROR} " +
            "No available transport route selected [service={}]",
            invocation.serviceId.asText())
        invocation.complete(null, ClientNoTransportRouteException())
      }
      return
    }
    invocation.routes = routes
    chain.filter(invocation)
  }
}