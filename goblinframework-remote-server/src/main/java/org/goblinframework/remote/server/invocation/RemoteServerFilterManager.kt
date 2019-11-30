package org.goblinframework.remote.server.invocation

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.service.ServiceInstaller
import org.goblinframework.remote.server.invocation.endpoint.RemoteServerInvocationEndpoint

@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.invocation")
class RemoteServerFilterManager private constructor()
  : GoblinManagedObject(), RemoteServerFilterManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerFilterManager()
  }

  private val customized = mutableListOf<RemoteServerFilterDelegator>()

  init {
    ServiceInstaller.asList(RemoteServerFilter::class.java)
        .map { RemoteServerFilterDelegator(it) }
        .sortedBy { it.order }
        .forEach { customized.add(it) }
  }

  fun createFilterChain(): RemoteServerFilterChain {
    val filters = mutableListOf<RemoteServerFilter>()
    filters.addAll(customized)
    filters.add(RemoteServerInvocationEndpoint.INSTANCE)
    return RemoteServerFilterChainImpl(filters)
  }

  class RemoteServerFilterChainImpl(private val filterList: List<RemoteServerFilter>) : RemoteServerFilterChain {
    private var position = 0
    override fun filter(context: RemoteServerInvocation) {
      if (position < filterList.size) {
        val filter = filterList[position++]
        filter.filter(context, this)
      }
    }
  }
}