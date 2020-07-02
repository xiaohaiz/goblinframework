package org.goblinframework.remote.client.invocation.filter

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.client.invocation.RemoteClientFilterMXBean
import org.goblinframework.remote.client.invocation.RemoteClientInvocation
import org.goblinframework.remote.client.invocation.RpcClientFilter
import org.goblinframework.rpc.filter.RpcFilterChain
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.invocation")
abstract class AbstractInternalFilter
  : GoblinManagedObject(), RpcClientFilter, RemoteClientFilterMXBean {

  private val executionCount = LongAdder()

  override fun getOrder(): Int {
    throw UnsupportedOperationException()
  }

  override fun filter(invocation: RemoteClientInvocation, chain: RpcFilterChain<RemoteClientInvocation>) {
    executionCount.increment()
    doFilter(invocation, chain)
  }

  abstract fun doFilter(invocation: RemoteClientInvocation, chain: RpcFilterChain<RemoteClientInvocation>)

  override fun getName(): String {
    return javaClass.name
  }

  override fun getExecutionCount(): Long {
    return executionCount.sum()
  }
}