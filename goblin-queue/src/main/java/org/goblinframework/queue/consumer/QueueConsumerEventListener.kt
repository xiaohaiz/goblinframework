package org.goblinframework.queue.consumer

import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener

@GoblinEventChannel("/goblin/queue/consumer")
class QueueConsumerEventListener : GoblinEventListener {
  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is QueueConsumerEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as QueueConsumerEvent
    event.recordListeners.forEach(ConsumerRecordListener::onReceived)
    event.executors.onEvent(event)
  }

}