package org.goblinframework.remote.client.service

import org.goblinframework.api.registry.RegistryPathListener
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.util.CollectionUtils
import org.goblinframework.core.util.HttpUtils
import org.goblinframework.core.util.StringUtils
import org.goblinframework.remote.core.service.RemoteServiceId
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read

@GoblinManagedBean(type = "RemoteClient")
class RemoteClient
internal constructor(private val serviceId: RemoteServiceId)
  : GoblinManagedObject(), RemoteClientMXBean {

  private val listener: RegistryPathListener
  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<String, String>()

  init {
    val registry = RemoteClientRegistryManager.INSTANCE.getRegistry()!!
    listener = registry.createPathListener()
        .path("/goblin/remote/service/${serviceId.interfaceClass.name}")
        .handler { onNodes(it) }
    listener.initialize()
  }

  private fun onNodes(nodes: List<String>) {
    val recognized = nodes.filter {
      val qs = StringUtils.substringAfterLast(it, "/")
      val map = HttpUtils.parseQueryString(qs)
      serviceId.group == map["group"] && serviceId.version == map["version"]
    }.toList()
    val delta = lock.read {
      val base = buffer.keys.toList()
      CollectionUtils.calculateCollectionDelta(base, recognized)
    }
    if (delta.isEmpty) {
      return
    }
    delta.deletionList.forEach { remove(it) }
    delta.neonatalList.forEach { create(it) }
  }

  private fun remove(node: String) {}

  private fun create(node: String) {}

  override fun disposeBean() {
    listener.dispose()
  }
}