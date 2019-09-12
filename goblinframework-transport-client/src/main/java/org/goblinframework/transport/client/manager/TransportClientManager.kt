package org.goblinframework.transport.client.manager

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.concurrent.SynchronizedCountLatch
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean("TRANSPORT.CLIENT")
class TransportClientManager private constructor() : GoblinManagedObject(), TransportClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = TransportClientManager()
  }

  fun closeConnection(name: String) {}

  fun close() {
    unregisterIfNecessary()
    DisconnectFutureManager.INSTANCE.close()
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

    internal fun close(): Boolean {
      return try {
        latch.awaitUninterruptibly(15, TimeUnit.SECONDS)
        true
      } catch (e: Exception) {
        false
      }
    }
  }
}