package org.goblinframework.core.event

import com.lmax.disruptor.WorkHandler
import org.goblinframework.core.util.OrderUtils

class EventBusBossEventHandler private constructor() : WorkHandler<EventBusBossEvent> {

  companion object {
    @JvmField val INSTANCE = EventBusBossEventHandler()
  }

  override fun onEvent(event: EventBusBossEvent) {
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
      ctx.complete(GoblinEventState.CHANNEL_NOT_REGISTERED)
      return
    }
    val listeners = worker.lookup(ctx)
    if (listeners.isEmpty()) {
      ctx.complete(GoblinEventState.LISTENER_NOT_SUBSCRIBED)
      return
    }
    if (ctx.event.fair) {
      val sorted = listeners.sortedWith(Comparator { o1, o2 ->
        OrderUtils.calculateOrder(o1).compareTo(OrderUtils.calculateOrder(o2))
      }).toList()
      ctx.initializeTaskCount(1)
      worker.public(ctx, sorted)
    } else {
      ctx.initializeTaskCount(listeners.size)
      listeners.forEach { worker.public(ctx, listOf(it)) }
    }
  }
}