package org.goblinframework.remote.client.service

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.system.GoblinSystem
import org.goblinframework.core.util.CollectionUtils
import org.goblinframework.core.util.HttpUtils
import org.goblinframework.registry.zookeeper.ZookeeperRegistryPathWatcher
import org.goblinframework.remote.client.transport.RemoteTransportClient
import org.goblinframework.remote.client.transport.RemoteTransportClientManager
import org.goblinframework.rpc.service.RemoteServiceId
import org.goblinframework.transport.client.channel.TransportClient
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.service")
class RemoteServiceClient
internal constructor(private val serviceId: RemoteServiceId,
                     private val subscriptionManager: RemoteServiceClientSubscriptionManager?)
  : GoblinManagedObject(), RemoteServiceClientMXBean {

  private val watcherReference = AtomicReference<ZookeeperRegistryPathWatcher?>()
  private val firstTime = AtomicBoolean()
  private val lock = ReentrantReadWriteLock()
  private val connections = mutableMapOf<String, RemoteTransportClient>()
  private val future = RemoteServiceClientFuture()
  private val router = RemoteTransportClientRouter()

  init {
    subscriptionManager?.run {
      val registry = this.registry
      val watcher = registry.createWatcher()
          .path("/goblin/remote/service/${serviceId.serviceInterface}/server")
          .reload(1, TimeUnit.MINUTES)
          .handler { onNodes(it) }
      watcher.initialize()
      watcherReference.set(watcher)
      this.watch(serviceId.serviceInterface)
    }
  }

  fun future(): RemoteServiceClientFuture {
    return future
  }

  private fun onNodes(nodes: List<String>) {
    val firstTime = this.firstTime.compareAndSet(false, true)
    val recognized = nodes.filter {
      val map = HttpUtils.parseQueryString(it)
      val clientVersion = serviceId.serviceVersion
      val clientDomain = GoblinSystem.runtimeMode().name
      return@filter clientVersion == map["serverVersion"] && clientDomain == map["serverDomain"]
    }.toList()
    val delta = lock.read {
      val base = connections.keys.toList()
      CollectionUtils.calculateCollectionDelta(base, recognized)
    }
    if (firstTime) {
      if (delta.neonatalList.isEmpty()) {
        future.complete(this)
        return
      } else {
        future.initializeReferenceCount(delta.neonatalList.size)
      }
    }
    delta.deletionList.forEach { remove(it) }
    delta.neonatalList.forEach { create(it, firstTime) }
  }

  private fun remove(node: String) {
    lock.write { connections.remove(node) }?.run {
      router.removeConnection(this)
      this.release(this@RemoteServiceClient)
    }
  }

  private fun create(node: String, firstTime: Boolean) {
    lock.write {
      connections[node]?.run { return }
      val connection = RemoteTransportClientManager.INSTANCE.openConnection(node)
      connection.retain(this)
      val connectFuture = connection.transportClient().connectFuture()
      if (firstTime) {
        connectFuture.addListener {
          try {
            if (it.get()!!.available()) {
              future.finishConnection(this)
            } else {
              future.finishConnection(null)
            }
          } catch (ex: Exception) {
            future.finishConnection(null)
          }
        }
      }
      connections[node] = connection
    }
  }

  fun addConnection(connection: RemoteTransportClient) {
    router.addConnection(connection)
  }

  fun selectTransportClient(): TransportClient? {
    return router.selectRemoteTransportClient()?.transportClient()
  }

  fun selectTransportClients(): List<TransportClient> {
    return router.selectRemoteTransportClients().map { it.transportClient() }.toList()
  }

  override fun disposeBean() {
    subscriptionManager?.unwatch(serviceId.serviceInterface)
    watcherReference.getAndSet(null)?.dispose()
    lock.write {
      connections.values.forEach { it.release(this) }
      connections.clear()
    }
    router.dispose()
  }
}