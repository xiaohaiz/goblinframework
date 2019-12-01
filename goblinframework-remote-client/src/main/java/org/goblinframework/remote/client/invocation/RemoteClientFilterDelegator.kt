package org.goblinframework.remote.client.invocation

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.core.filter.RemoteFilterChain
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean("RemoteClient")
class RemoteClientFilterDelegator
internal constructor(private val filter: RemoteClientFilter)
  : GoblinManagedObject(), RemoteClientFilter, RemoteClientFilterMXBean {

  private val executionCount = LongAdder()

  override fun getOrder(): Int {
    return filter.order
  }

  override fun filter(invocation: RemoteClientInvocation, chain: RemoteFilterChain<RemoteClientInvocation>) {
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