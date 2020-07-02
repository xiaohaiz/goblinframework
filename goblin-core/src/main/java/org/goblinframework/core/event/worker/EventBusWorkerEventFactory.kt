package org.goblinframework.core.event.worker

import com.lmax.disruptor.EventFactory

class EventBusWorkerEventFactory private constructor() : EventFactory<EventBusWorkerEvent> {

  companion object {
    @JvmField val INSTANCE = EventBusWorkerEventFactory()
  }

  override fun newInstance(): EventBusWorkerEvent {
    return EventBusWorkerEvent()
  }

}