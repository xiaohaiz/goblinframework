package org.goblinframework.remote.server.invocation

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.rpc.filter.RpcFilterChain
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean("RemoteServer")
class RpcServerFilterDelegator
internal constructor(private val filter: RpcServerFilter)
  : GoblinManagedObject(), RemoteServerFilterMXBean, RpcServerFilter {

  private val executionCount = LongAdder()

  override fun getOrder(): Int {
    return filter.order
  }

  override fun filter(invocation: RemoteServerInvocation, chain: RpcFilterChain<RemoteServerInvocation>) {
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