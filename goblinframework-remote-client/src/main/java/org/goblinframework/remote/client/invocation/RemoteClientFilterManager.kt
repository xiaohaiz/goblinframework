package org.goblinframework.remote.client.invocation

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.service.ServiceInstaller
import org.goblinframework.remote.client.invocation.endpoint.RemoteClientInvocationEndpoint
import org.goblinframework.remote.client.invocation.filter.CreateFlightFilter
import org.goblinframework.remote.client.invocation.filter.EncodeRequestFilter
import org.goblinframework.remote.client.invocation.filter.SelectRouterFilter

@Singleton
@GoblinManagedBean("RemoveClient")
class RemoteClientFilterManager private constructor()
  : GoblinManagedObject(), RemoteClientFilterManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientFilterManager()
  }

  private val customized = mutableListOf<RemoteClientFilterDelegator>()

  init {
    ServiceInstaller.asList(RemoteClientFilter::class.java)
        .map { RemoteClientFilterDelegator(it) }
        .sortedBy { it.order }
        .forEach { customized.add(it) }
  }

  fun createFilterChain(): RemoteClientFilterChain {
    val filters = mutableListOf<RemoteClientFilter>()
    filters.add(EncodeRequestFilter.INSTANCE)
    filters.add(CreateFlightFilter.INSTANCE)
    filters.add(SelectRouterFilter.INSTANCE)
    filters.addAll(customized)
    filters.add(RemoteClientInvocationEndpoint.INSTANCE)
    return RemoteClientFilterChainImpl(filters)
  }

  override fun disposeBean() {
    RemoteClientInvocationEndpoint.INSTANCE.dispose()
    customized.sortedByDescending { it.order }.forEach { it.dispose() }
    SelectRouterFilter.INSTANCE.dispose()
    CreateFlightFilter.INSTANCE.dispose()
    EncodeRequestFilter.INSTANCE.dispose()
  }

  class RemoteClientFilterChainImpl(private val filterList: List<RemoteClientFilter>)
    : RemoteClientFilterChain {
    private var position = 0
    override fun filter(context: RemoteClientInvocation) {
      if (position < filterList.size) {
        val filter = filterList[position++]
        filter.filter(context, this)
      }
    }
  }
}