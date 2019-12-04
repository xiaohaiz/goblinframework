package org.goblinframework.core.event.boss

import com.lmax.disruptor.WorkHandler
import org.goblinframework.core.event.exception.EventBossChannelNotFoundException
import org.goblinframework.core.event.exception.EventBossListenerNotFoundException
import org.goblinframework.core.util.ObjectUtils

class EventBusBossEventHandler private constructor() : WorkHandler<EventBusBossEvent> {

  companion object {
    @JvmField val INSTANCE = EventBusBossEventHandler()
  }

  override fun onEvent(event: EventBusBossEvent) {
    event.receivedCount?.increment()
    try {
      processEventBusBossEvent(event)
    } finally {
      event.clear()
    }
  }

  private fun processEventBusBossEvent(event: EventBusBossEvent) {
    val ctx = event.ctx!!
    val worker = EventBusBoss.INSTANCE.lookup(ctx.channel)
    if (worker == null) {
      event.workerMissedCount?.increment()
      ctx.bossExceptionCaught(EventBossChannelNotFoundException())
      ctx.complete()
      return
    }
    val listeners = worker.lookup(ctx)
    if (listeners.isEmpty()) {
      event.listenerMissedCount?.increment()
      ctx.bossExceptionCaught(EventBossListenerNotFoundException())
      ctx.complete()
      return
    }
    if (ctx.event.isFair) {
      val sorted = listeners.sortedWith(Comparator { o1, o2 ->
        ObjectUtils.calculateOrder(o1).compareTo(ObjectUtils.calculateOrder(o2))
      }).toList()
      ctx.initializeTask(1)
      worker.publish(0, ctx, sorted)
      event.dispatchedCount?.increment()
    } else {
      ctx.initializeTask(listeners.size)
      listeners.forEachIndexed { taskId, listener ->
        worker.publish(taskId, ctx, listOf(listener))
        event.dispatchedCount?.increment()
      }
    }
  }
}