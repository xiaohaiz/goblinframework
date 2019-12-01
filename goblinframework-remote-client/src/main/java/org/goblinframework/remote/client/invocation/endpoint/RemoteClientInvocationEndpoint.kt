package org.goblinframework.remote.client.invocation.endpoint

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.client.invocation.RemoteClientEndpoint
import org.goblinframework.remote.client.invocation.RemoteClientEndpointMXBean
import org.goblinframework.remote.client.invocation.RemoteClientInvocation
import org.goblinframework.remote.core.filter.RemoteFilterChain
import java.util.concurrent.atomic.LongAdder

@Singleton
@GoblinManagedBean("RemoteClient")
class RemoteClientInvocationEndpoint private constructor()
  : GoblinManagedObject(), RemoteClientEndpoint, RemoteClientEndpointMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientInvocationEndpoint()
  }

  private val executionCount = LongAdder()

  override fun filter(invocation: RemoteClientInvocation, chain: RemoteFilterChain<RemoteClientInvocation>) {
    println("!")
  }

  override fun getName(): String {
    return "RemoteClientInvocationEndpoint"
  }

  override fun getExecutionCount(): Long {
    return executionCount.sum()
  }
}