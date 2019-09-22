package org.goblinframework.transport.server.channel

import io.netty.channel.Channel
import io.netty.channel.ChannelId
import org.goblinframework.core.concurrent.SynchronizedCountLatch
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean("TRANSPORT.SERVER")
class TransportServerChannelManager(private val server: TransportServerImpl)
  : GoblinManagedObject(), TransportServerChannelManagerMXBean {

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<ChannelId, TransportServerChannel>()
  private val latch = SynchronizedCountLatch()
  private val terminated = AtomicBoolean()

  fun getChannel(id: ChannelId): TransportServerChannel? {
    return lock.read { buffer[id] }
  }

  fun register(channel: Channel) {
    if (terminated.get()) {
      return
    }
    val id = channel.id()
    lock.write {
      if (buffer[id] == null) {
        val serverChannel = TransportServerChannel(server, channel)
        buffer[id] = serverChannel
        latch.countUp()
      }
    }
  }

  fun unregister(id: ChannelId) {
    lock.write {
      buffer.remove(id)?.run {
        this.dispose()
        latch.countDown()
      }
    }
  }

  fun terminate() {
    if (!terminated.compareAndSet(false, true)) {
      return
    }
    lock.read { buffer.values.forEach { it.terminate() } }
  }

  fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean {
    return try {
      latch.awaitUninterruptibly(timeout, unit)
      true
    } catch (e: Exception) {
      false
    }
  }

  override fun disposeBean() {
    lock.write {
      buffer.values.forEach { it.dispose() }
      buffer.clear()
    }
  }

}