package org.goblinframework.transport.client.flight

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.DateFormatUtils
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean(type = "transport.client")
class MessageFlightPartition
internal constructor(private val id: Int)
  : GoblinManagedObject(), MessageFlightPartitionMXBean {

  private val buffer = ConcurrentHashMap<Long, MessageFlight>()
  private val attachCount = LongAdder()
  private val detachCount = LongAdder()
  private val expireCount = LongAdder()
  private val lastActiveTimestamp = AtomicLong(-1)

  internal fun attach(flight: MessageFlight) {
    val previous = buffer.put(flight.id(), flight)
    if (previous == null) {
      attachCount.increment()
      lastActiveTimestamp.set(System.currentTimeMillis())
    }
  }

  internal fun detach(id: Long): MessageFlight? {
    val flight = buffer.remove(id)
    flight?.run {
      detachCount.increment()
      lastActiveTimestamp.set(System.currentTimeMillis())
    }
    return flight
  }

  internal fun clearExpired() {
    val expiredIds = buffer.values.filter { it.expired() }.map { it.id() }.toSet()
    expiredIds.forEach {
      buffer.remove(it)?.run {
        expireCount.increment()
        lastActiveTimestamp.set(System.currentTimeMillis())
      }
    }
  }

  override fun getId(): Int {
    return id
  }

  override fun getSize(): Int {
    return buffer.size
  }

  override fun getAttachCount(): Long {
    return attachCount.sum()
  }

  override fun getDetachCount(): Long {
    return detachCount.sum()
  }

  override fun getExpireCount(): Long {
    return expireCount.sum()
  }

  override fun getLastActiveTimestamp(): String? {
    val ts = lastActiveTimestamp.get()
    if (ts < 0) {
      return null
    } else {
      return DateFormatUtils.format(ts)
    }
  }
}