package org.goblinframework.monitor.message

import org.goblinframework.api.core.*
import org.goblinframework.api.event.EventBus
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
@GoblinManagedBean(type = "monitor")
class TimedTouchableMessageBufferManager private constructor()
  : GoblinManagedObject(), TimedTouchableMessageBufferManagerMXBean {

  companion object {
    @JvmField val INSTANCE = TimedTouchableMessageBufferManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val actions = mutableListOf<Block0>()
  private val scheduler = TimedTouchableMessageBufferScheduler(this)

  init {
    EventBus.subscribe(scheduler)
  }

  fun registerAction(action: Block0) {
    lock.write { actions.add(action) }
  }

  internal fun executeActions() {
    val candidates = lock.read { actions.toList() }
    candidates.forEach {
      try {
        it.apply()
      } catch (ignore: Exception) {
      }
    }
  }

  override fun disposeBean() {
    EventBus.unsubscribe(scheduler)
  }
}
