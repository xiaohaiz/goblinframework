package org.goblinframework.remote.client.service

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.EventBus
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.timer.SecondTimerEventListener
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

@Singleton
@GoblinManagedBean("RemoteClient")
class RemoteServiceClientWarmUpManager private constructor()
  : GoblinManagedObject(), RemoteServiceClientWarmUpManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServiceClientWarmUpManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val queue = mutableListOf<RemoteTransportClientRouter.MutableRemoteTransportClient>()
  private val eventListenerReference = AtomicReference<SecondTimerEventListener?>()

  override fun initializeBean() {
    val listener: SecondTimerEventListener = object : SecondTimerEventListener() {

      override fun periodSeconds(): Long {
        return 5
      }

      override fun onEvent(context: GoblinEventContext) {
        scan()
      }
    }
    EventBus.subscribe(listener)
    eventListenerReference.set(listener)
  }

  internal fun register(mutable: RemoteTransportClientRouter.MutableRemoteTransportClient) {
    check(eventListenerReference.get() != null) { "Initialization is required" }
    lock.write { queue.add(mutable) }
  }

  private fun scan() {
    val candidates = mutableListOf<RemoteTransportClientRouter.MutableRemoteTransportClient>()
    val current = System.currentTimeMillis()
    lock.write {
      val it = queue.iterator()
      while (it.hasNext()) {
        val mutable = it.next()
        if (current - mutable.createTime >= 30000) {
          it.remove()
          candidates.add(mutable)
        }
      }
    }
    if (candidates.isEmpty()) {
      return
    }
    candidates.forEach { it.router.restoreWeight(it.connection.getId()) }
  }

  override fun disposeBean() {
    eventListenerReference.getAndSet(null)?.run {
      EventBus.unsubscribe(this)
    }
    lock.write { queue.clear() }
  }
}