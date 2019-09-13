package org.goblinframework.transport.client.flight

import org.goblinframework.core.event.EventBus
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.transport.core.protocol.TransportResponse
import java.util.concurrent.atomic.AtomicBoolean

@GoblinManagedBean("TRANSPORT:CLIENT")
class MessageFlightManager private constructor() : GoblinManagedObject(), MessageFlightManagerMXBean {

  companion object {
    private const val PARTITION_COUNT = 10
    @JvmField val INSTANCE = MessageFlightManager()
  }

  private val partitions = Array(PARTITION_COUNT) { MessageFlightPartition(it) }
  private val active = AtomicBoolean()

  fun initialize() {
    EventBus.subscribe(MessageFlightManagerSweeper.INSTANCE)
  }

  fun createMessageFlight(response: Boolean = true): MessageFlight {
    val flight = MessageFlight(this, response)
    val id = flight.id()
    lookupPartition(id).attach(flight)
    active.set(true)
    return flight
  }

  fun onResponse(response: TransportResponse) {
    val id = response.requestId
    val partition = lookupPartition(id)
    partition.detach(id)?.run {
      this.complete(response)
    }
  }

  fun close() {
    unregisterIfNecessary()
    EventBus.unsubscribe(MessageFlightManagerSweeper.INSTANCE)
    partitions.forEach { it.close() }
  }

  internal fun clearExpired() {
    partitions.forEach { it.clearExpired() }
  }

  private fun lookupPartition(id: Long): MessageFlightPartition {
    val index = (id % PARTITION_COUNT).toInt()
    return partitions[index]
  }

  override fun getActive(): Boolean {
    return active.get()
  }

  override fun getMessageFlightPartitionList(): Array<MessageFlightPartitionMXBean> {
    return partitions.toList().toTypedArray()
  }
}