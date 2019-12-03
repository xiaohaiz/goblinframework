package org.goblinframework.core.event.boss

import com.lmax.disruptor.WorkHandler
import org.goblinframework.core.event.EventBusBossEvent
import org.goblinframework.core.event.ListenerNotFoundException
import org.goblinframework.core.event.WorkerNotFoundException
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
      ctx.exceptionCaught(WorkerNotFoundException(ctx.channel))
      ctx.complete()
      return
    }
    val listeners = worker.lookup(ctx)
    if (listeners.isEmpty()) {
      event.listenerMissedCount?.increment()
      ctx.exceptionCaught(ListenerNotFoundException(ctx.channel))
      ctx.complete()
      return
    }
    if (ctx.event.isFair) {
      val sorted = listeners.sortedWith(Comparator { o1, o2 ->
        ObjectUtils.calculateOrder(o1).compareTo(ObjectUtils.calculateOrder(o2))
      }).toList()
      ctx.initializeTaskCount(1)
      worker.public(ctx, sorted)
      event.dispatchedCount?.increment()
    } else {
      ctx.initializeTaskCount(listeners.size)
      listeners.forEach {
        worker.public(ctx, listOf(it))
        event.dispatchedCount?.increment()
      }
    }
  }
}