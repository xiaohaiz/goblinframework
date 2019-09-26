package org.goblinframework.remote.client.service

import org.goblinframework.api.registry.RegistryPathListener
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.util.CollectionUtils
import org.goblinframework.core.util.HttpUtils
import org.goblinframework.remote.client.connection.RemoteConnection
import org.goblinframework.remote.client.connection.RemoteConnectionManager
import org.goblinframework.remote.core.service.RemoteServiceId
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "RemoteClient")
class RemoteClient
internal constructor(private val serviceId: RemoteServiceId)
  : GoblinManagedObject(), RemoteClientMXBean {

  private val listener: RegistryPathListener
  private val lock = ReentrantReadWriteLock()
  private val connections = mutableMapOf<String, RemoteConnection>()

  init {
    val registry = RemoteClientRegistryManager.INSTANCE.getRegistry()!!
    listener = registry.createPathListener()
        .path("/goblin/remote/service/${serviceId.interfaceClass.name}")
        .handler { onNodes(it) }
    listener.initialize()
  }

  private fun onNodes(nodes: List<String>) {
    val recognized = nodes.filter {
      val map = HttpUtils.parseQueryString(it)
      serviceId.group == map["group"] && serviceId.version == map["version"]
    }.toList()
    val delta = lock.read {
      val base = connections.keys.toList()
      CollectionUtils.calculateCollectionDelta(base, recognized)
    }
    if (delta.isEmpty) {
      return
    }
    delta.deletionList.forEach { remove(it) }
    delta.neonatalList.forEach { create(it) }
  }

  private fun remove(node: String) {
    lock.write { connections.remove(node) }?.run {
      RemoteConnectionManager.INSTANCE.closeConnection(this.id())
    }
  }

  private fun create(node: String) {
    lock.write {
      connections[node]?.run { return }
      val connection = RemoteConnectionManager.INSTANCE.openConnection(node)
      connections[node] = connection
    }
  }

  fun availableConnections(): List<RemoteConnection> {
    return lock.read {
      connections.values.filter { it.transportClient.available() }.toList()
    }
  }

  override fun disposeBean() {
    listener.dispose()
  }
}