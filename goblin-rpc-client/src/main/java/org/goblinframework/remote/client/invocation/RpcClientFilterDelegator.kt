package org.goblinframework.remote.client.invocation

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.rpc.filter.RpcFilterChain
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean("RemoteClient")
class RpcClientFilterDelegator
internal constructor(private val filter: RpcClientFilter)
  : GoblinManagedObject(), RpcClientFilter, RemoteClientFilterMXBean {

  private val executionCount = LongAdder()

  override fun getOrder(): Int {
    return filter.order
  }

  override fun filter(invocation: RemoteClientInvocation, chain: RpcFilterChain<RemoteClientInvocation>) {
    executionCount.increment()
    filter.filter(invocation, chain)
  }

  override fun getName(): String {
    return filter.javaClass.name
  }

  override fun getExecutionCount(): Long {
    return executionCount.sum()
  }
}