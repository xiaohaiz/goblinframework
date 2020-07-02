package org.goblinframework.remote.client.invocation

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.service.ServiceInstaller
import org.goblinframework.remote.client.invocation.endpoint.RpcClientInvocationEndpoint
import org.goblinframework.remote.client.invocation.filter.CreateFlightFilter
import org.goblinframework.remote.client.invocation.filter.EncodeRequestFilter
import org.goblinframework.remote.client.invocation.filter.SelectRouteFilter

@Singleton
@GoblinManagedBean("RemoveClient")
class RemoteClientFilterManager private constructor()
  : GoblinManagedObject(), RemoteClientFilterManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientFilterManager()
  }

  private val customized = mutableListOf<RpcClientFilterDelegator>()

  init {
    ServiceInstaller.asList(RpcClientFilter::class.java)
        .map { RpcClientFilterDelegator(it) }
        .sortedBy { it.order }
        .forEach { customized.add(it) }
  }

  fun createFilterChain(): RpcClientFilterChain {
    val filters = mutableListOf<RpcClientFilter>()
    filters.add(EncodeRequestFilter.INSTANCE)
    filters.add(CreateFlightFilter.INSTANCE)
    filters.add(SelectRouteFilter.INSTANCE)
    filters.addAll(customized)
    filters.add(RpcClientInvocationEndpoint.INSTANCE)
    return RpcClientFilterChainImpl(filters)
  }

  override fun disposeBean() {
    RpcClientInvocationEndpoint.INSTANCE.dispose()
    customized.sortedByDescending { it.order }.forEach { it.dispose() }
    SelectRouteFilter.INSTANCE.dispose()
    CreateFlightFilter.INSTANCE.dispose()
    EncodeRequestFilter.INSTANCE.dispose()
  }

  class RpcClientFilterChainImpl(private val filterList: List<RpcClientFilter>)
    : RpcClientFilterChain {
    private var position = 0
    override fun filter(context: RemoteClientInvocation) {
      if (position < filterList.size) {
        val filter = filterList[position++]
        filter.filter(context, this)
      }
    }
  }
}