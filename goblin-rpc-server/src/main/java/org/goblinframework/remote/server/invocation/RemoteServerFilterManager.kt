package org.goblinframework.remote.server.invocation

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.service.ServiceInstaller
import org.goblinframework.remote.server.invocation.endpoint.RpcServerInvocationEndpoint
import org.goblinframework.remote.server.invocation.filter.LocateMethodFilter
import org.goblinframework.remote.server.invocation.filter.LocateServiceFilter
import org.goblinframework.remote.server.invocation.filter.ResolveArgumentFilter
import org.goblinframework.remote.server.invocation.filter.SendResponseFilter

@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.invocation")
class RemoteServerFilterManager private constructor()
  : GoblinManagedObject(), RemoteServerFilterManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerFilterManager()
  }

  private val customized = mutableListOf<RpcServerFilterDelegator>()

  init {
    ServiceInstaller.asList(RpcServerFilter::class.java)
        .map { RpcServerFilterDelegator(it) }
        .sortedBy { it.order }
        .forEach { customized.add(it) }
  }

  fun createFilterChain(): RpcServerFilterChain {
    val filters = mutableListOf<RpcServerFilter>()
    filters.add(SendResponseFilter.INSTANCE)
    filters.add(LocateServiceFilter.INSTANCE)
    filters.add(LocateMethodFilter.INSTANCE)
    filters.add(ResolveArgumentFilter.INSTANCE)
    filters.addAll(customized)
    filters.add(RpcServerInvocationEndpoint.INSTANCE)
    return RpcServerFilterChainImpl(filters)
  }

  override fun disposeBean() {
    RpcServerInvocationEndpoint.INSTANCE.dispose()
    customized.sortedByDescending { it.order }.forEach { it.dispose() }
    ResolveArgumentFilter.INSTANCE.dispose()
    LocateMethodFilter.INSTANCE.dispose()
    LocateServiceFilter.INSTANCE.dispose()
    SendResponseFilter.INSTANCE.dispose()
  }

  class RpcServerFilterChainImpl(private val filterList: List<RpcServerFilter>) : RpcServerFilterChain {
    private var position = 0
    override fun filter(context: RemoteServerInvocation) {
      if (position < filterList.size) {
        val filter = filterList[position++]
        filter.filter(context, this)
      }
    }
  }
}