package org.goblinframework.queue.producer

import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.slf4j.LoggerFactory

@GoblinEventChannel("/goblin/queue/producer")
class QueueProducerEventListener : GoblinEventListener {

  companion object {
    private val logger = LoggerFactory.getLogger(QueueProducerEventListener::class.java)
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is QueueProducerEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as QueueProducerEvent
    try {
      event.producer.send(event.data)
    } catch (e: Exception) {
      logger.warn("Failed to produce message", e)
    } finally {
      event.future.complete(1)
    }
  }
}
