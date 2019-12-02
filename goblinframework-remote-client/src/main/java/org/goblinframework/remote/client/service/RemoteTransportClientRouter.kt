package org.goblinframework.remote.client.service

import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.client.transport.RemoteTransportClient
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@ThreadSafe
@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.service")
class RemoteTransportClientRouter internal constructor()
  : GoblinManagedObject(), RemoteTransportClientRouterMXBean {

  private val lock = ReentrantReadWriteLock()
  private val buffer = LinkedHashMap<String, MutableRemoteTransportClient>()
  private val routerTable = mutableListOf<WeightedRemoteTransportClient>()
  private val routerCounter = AtomicLong()

  fun addConnection(connection: RemoteTransportClient) {
    lock.write {
      if (buffer[connection.getId()] != null) {
        return
      }
      val mutable = MutableRemoteTransportClient(this, connection)
      buffer[connection.getId()] = mutable
      if (mutable.requireWarmUp()) {
        RemoteServiceClientWarmUpManager.INSTANCE.register(mutable)
      }
      if (logger.isDebugEnabled) {
        logger.debug("+ (${buffer.size}) $connection")
      }
      val size = calculateRouterTable()
      if (logger.isDebugEnabled) {
        logger.debug("Router table (re)calculated, size is {}", size)
      }
    }
  }

  fun removeConnection(connection: RemoteTransportClient) {
    lock.write {
      buffer.remove(connection.getId())?.run {
        if (logger.isDebugEnabled) {
          logger.debug("- (${buffer.size}) ${this.connection}")
        }
        val size = calculateRouterTable()
        if (logger.isDebugEnabled) {
          logger.debug("Router table (re)calculated, size is {}", size)
        }
      }
    }
  }

  fun restoreWeight(id: String) {
    lock.write {
      buffer[id]?.run {
        this.restore()
        if (logger.isDebugEnabled) {
          logger.debug("{} weight restored to {}", connection, connection.getWeight())
        }
        val size = calculateRouterTable()
        if (logger.isDebugEnabled) {
          logger.debug("Router table (re)calculated, size is {}", size)
        }
      }
    }
  }

  private fun calculateRouterTable(): Int {
    if (buffer.isEmpty()) {
      routerTable.clear()
      routerCounter.set(0)
      return 0
    }
    val nodes = mutableListOf<WeightedRemoteTransportClient>()
    val allClients = buffer.values.map { WeightedRemoteTransportClient(it) }.toList()
    val totalWeight = allClients.sumBy { it.effectiveWeight }

    for (i in 0 until totalWeight) {
      allClients.forEach {
        it.currentWeight = it.currentWeight + it.effectiveWeight
      }
      val node = allClients.maxBy { it.currentWeight }!!
      node.currentWeight = node.currentWeight - totalWeight
      nodes.add(node)
    }

    routerTable.clear()
    routerTable.addAll(nodes)
    routerCounter.set(0)
    return routerTable.size
  }

  fun selectRemoteTransportClient(): RemoteTransportClient? {
    return lock.read {
      if (routerTable.isEmpty()) {
        return null
      }
      val index = (routerCounter.getAndIncrement() % routerTable.size).toInt()
      val mutable = routerTable[index].mutable
      mutable.connection.transportClient().retain()
      mutable.connection
    }
  }

  fun selectRemoteTransportClients(): List<RemoteTransportClient> {
    return lock.read {
      val connections = buffer.values.map { it.connection }.toList()
      connections.forEach {
        it.transportClient().retain()
      }
      connections
    }
  }

  override fun disposeBean() {
    lock.write {
      buffer.clear()
      routerTable.clear()
      routerCounter.set(0)
    }
  }

  class MutableRemoteTransportClient(val router: RemoteTransportClientRouter,
                                     val connection: RemoteTransportClient) {

    val createTime = System.currentTimeMillis()
    private val weight = AtomicInteger(1)

    fun weight(): Int {
      return weight.get()
    }

    fun requireWarmUp(): Boolean {
      return connection.getWeight() > 1
    }

    fun restore() {
      weight.set(connection.getWeight())
    }
  }

  class WeightedRemoteTransportClient(val mutable: MutableRemoteTransportClient) {
    var effectiveWeight = mutable.weight()
    var currentWeight = 0
  }
}