package org.goblinframework.remote.server.invocation.filter

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.remote.core.filter.RemoteFilterChain
import org.goblinframework.remote.server.dispatcher.response.RemoteServerResponseDispatcher
import org.goblinframework.remote.server.invocation.RemoteServerInvocation


@Singleton
class SendResponseFilter private constructor() : AbstractInternalFilter() {

  companion object {
    @JvmField val INSTANCE = SendResponseFilter()
  }

  override fun doFilter(invocation: RemoteServerInvocation, chain: RemoteFilterChain<RemoteServerInvocation>) {
    try {
      chain.filter(invocation)
    } finally {
      RemoteServerResponseDispatcher.INSTANCE.onResponse(invocation)
    }
  }
}