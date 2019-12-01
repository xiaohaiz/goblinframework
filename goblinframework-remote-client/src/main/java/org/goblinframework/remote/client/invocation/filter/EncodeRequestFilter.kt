package org.goblinframework.remote.client.invocation.filter

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.remote.client.invocation.RemoteClientInvocation
import org.goblinframework.remote.core.filter.RemoteFilterChain

@Singleton
class EncodeRequestFilter private constructor() : AbstractInternalFilter() {

  companion object {
    @JvmField val INSTANCE = EncodeRequestFilter()
  }

  override fun doFilter(invocation: RemoteClientInvocation, chain: RemoteFilterChain<RemoteClientInvocation>) {
    chain.filter(invocation)
  }
}