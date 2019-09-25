package org.goblinframework.core.event

import com.lmax.disruptor.EventFactory

class EventBusBossEventFactory private constructor() : EventFactory<EventBusBossEvent> {

  companion object {
    @JvmField val INSTANCE = EventBusBossEventFactory()
  }

  override fun newInstance(): EventBusBossEvent {
    return EventBusBossEvent()
  }
}