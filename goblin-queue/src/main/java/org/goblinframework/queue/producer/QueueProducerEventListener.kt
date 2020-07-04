package org.goblinframework.queue.producer

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener

@Install
@GoblinEventChannel("/goblin/queue/producer")
class QueueProducerEventListener : GoblinEventListener {
  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is QueueProducerEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as QueueProducerEvent
    event.producer.send(event.data)
    event.future.complete(1)
  }
}
