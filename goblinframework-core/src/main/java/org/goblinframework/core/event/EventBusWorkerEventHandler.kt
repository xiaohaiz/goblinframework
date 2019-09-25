package org.goblinframework.core.event

import com.lmax.disruptor.WorkHandler

class EventBusWorkerEventHandler private constructor() : WorkHandler<EventBusWorkerEvent> {

  companion object {
    @JvmField val INSTANCE = EventBusWorkerEventHandler()
  }

  override fun onEvent(event: EventBusWorkerEvent) {
    event.receivedCount?.increment()
    try {
      processEventBusWorkerEvent(event)
    } finally {
      event.clear()
    }
  }

  private fun processEventBusWorkerEvent(event: EventBusWorkerEvent) {
    val ctx = event.ctx!!
    val listeners = event.listeners!!
    try {
      listeners.forEach { it.onEvent(ctx) }
      event.succeedCount?.increment()
    } catch (ex: Exception) {
      event.failedCount?.increment()
      ctx.exceptionCaught(ex)
    } finally {
      ctx.finishTask()
    }
  }
}