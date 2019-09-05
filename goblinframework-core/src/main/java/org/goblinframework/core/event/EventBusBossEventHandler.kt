package org.goblinframework.core.event

import com.lmax.disruptor.WorkHandler

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

  }
}