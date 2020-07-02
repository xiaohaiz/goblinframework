package org.goblinframework.core.event.worker

import com.lmax.disruptor.WorkHandler
import org.goblinframework.core.event.exception.EventWorkerExecutionException

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
    } catch (ex: Throwable) {
      event.failedCount?.increment()
      ctx.workerExceptionCaught(event.taskId!!, EventWorkerExecutionException.rethrow(ex))
    } finally {
      ctx.finishTask()
    }
  }
}