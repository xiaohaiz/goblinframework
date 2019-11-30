package org.goblinframework.remote.server.invocation.endpoint

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.core.filter.RemoteFilterChain
import org.goblinframework.remote.server.invocation.RemoteServerEndpoint
import org.goblinframework.remote.server.invocation.RemoteServerEndpointMXBean
import org.goblinframework.remote.server.invocation.RemoteServerInvocation
import java.util.concurrent.atomic.LongAdder

@Singleton
@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.invocation")
class RemoteServerInvocationEndpoint private constructor()
  : GoblinManagedObject(), RemoteServerEndpoint, RemoteServerEndpointMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerInvocationEndpoint()
  }

  private val executionCount = LongAdder()
  private val successCount = LongAdder()
  private val failureCount = LongAdder()

  override fun filter(invocation: RemoteServerInvocation, chain: RemoteFilterChain<RemoteServerInvocation>) {
    executionCount.increment()

  }

  override fun getName(): String {
    return "RemoteServerInvocationEndpoint"
  }

  override fun getExecutionCount(): Long {
    return executionCount.sum()
  }

  override fun getSuccessCount(): Long {
    return successCount.sum()
  }

  override fun getFailureCount(): Long {
    return failureCount.sum()
  }
}