package org.goblinframework.core.event.boss

import com.lmax.disruptor.WorkHandler
import org.goblinframework.core.event.exception.ChannelNotFoundException
import org.goblinframework.core.event.exception.ListenerNotFoundException
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
      ctx.exceptionCaught(ChannelNotFoundException(ctx.channel))
      ctx.complete()
      return
    }
    val listeners = worker.lookup(ctx)
    if (listeners.isEmpty()) {
      ctx.exceptionCaught(ListenerNotFoundException(ctx.channel))
      ctx.complete()
      return
    }
    if (ctx.event.isFair) {
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