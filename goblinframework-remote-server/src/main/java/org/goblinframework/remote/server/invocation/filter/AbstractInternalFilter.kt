package org.goblinframework.remote.server.invocation.filter

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.core.filter.RemoteFilterChain
import org.goblinframework.remote.server.invocation.RemoteServerFilter
import org.goblinframework.remote.server.invocation.RemoteServerFilterMXBean
import org.goblinframework.remote.server.invocation.RemoteServerInvocation
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.invocation")
abstract class AbstractInternalFilter
  : GoblinManagedObject(), RemoteServerFilter, RemoteServerFilterMXBean {

  private val executionCount = LongAdder()

  override fun getName(): String {
    return javaClass.name
  }

  override fun getExecutionCount(): Long {
    return executionCount.sum()
  }

  override fun getOrder(): Int {
    throw UnsupportedOperationException()
  }

  override fun filter(invocation: RemoteServerInvocation, chain: RemoteFilterChain<RemoteServerInvocation>) {
    executionCount.increment()
    doFilter(invocation, chain)
  }

  abstract fun doFilter(invocation: RemoteServerInvocation, chain: RemoteFilterChain<RemoteServerInvocation>)

}