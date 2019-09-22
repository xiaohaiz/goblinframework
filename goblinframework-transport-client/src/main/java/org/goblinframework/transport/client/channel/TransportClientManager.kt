package org.goblinframework.transport.client.channel

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.concurrent.SynchronizedCountLatch
import org.goblinframework.core.event.EventBus
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.transport.client.setting.TransportClientSetting
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "transport.client")
class TransportClientManager private constructor() : GoblinManagedObject(), TransportClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = TransportClientManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<String, TransportClient>()

  override fun initializeBean() {
    EventBus.subscribe(TransportClientHeartbeatGenerator.INSTANCE)
  }

  fun getConnection(name: String): TransportClient? {
    return lock.read { buffer[name] }
  }

  fun createConnection(setting: TransportClientSetting): TransportClient {
    val name = setting.name()
    return lock.write {
      buffer[name]?.run {
        throw GoblinDuplicateException("Transport client [$name] already created")
      }
      val client = TransportClient(setting, this)
      buffer[name] = client
      client
    }
  }

  fun closeConnection(name: String) {
    lock.write { buffer.remove(name) }?.dispose()
  }

  fun sendHeartbeat() {
    lock.read { buffer.values.forEach { it.sendHeartbeat() } }
  }

  override fun disposeBean() {
    EventBus.unsubscribe(TransportClientHeartbeatGenerator.INSTANCE)
    lock.write {
      buffer.values.forEach { it.dispose() }
      buffer.clear()
    }
    DisconnectFutureManager.INSTANCE.dispose()
  }

  internal fun registerDisconnectFuture(future: TransportClientDisconnectFuture) {
    DisconnectFutureManager.INSTANCE.register(future)
  }

  internal fun unregisterDisconnectFuture(future: TransportClientDisconnectFuture) {
    DisconnectFutureManager.INSTANCE.unregister(future)
  }

  internal class DisconnectFutureManager private constructor() {
    companion object {
      @JvmField internal val INSTANCE = DisconnectFutureManager()
    }

    private val lock = ReentrantReadWriteLock()
    private val futures = IdentityHashMap<TransportClientDisconnectFuture, Instant>()
    private val latch = SynchronizedCountLatch()

    internal fun register(future: TransportClientDisconnectFuture) {
      lock.write {
        if (futures[future] == null) {
          futures[future] = Instant.now()
          latch.countUp()
        }
      }
    }

    internal fun unregister(future: TransportClientDisconnectFuture) {
      lock.write {
        futures.remove(future)?.run { latch.countDown() }
      }
    }

    internal fun dispose(): Boolean {
      return try {
        latch.awaitUninterruptibly(15, TimeUnit.SECONDS)
        true
      } catch (e: Exception) {
        false
      }
    }
  }
}