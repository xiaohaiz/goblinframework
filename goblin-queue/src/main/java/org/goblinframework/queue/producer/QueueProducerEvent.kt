package org.goblinframework.queue.producer

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.queue.SendResultFuture
import org.goblinframework.queue.api.QueueProducer

class QueueProducerEvent(private val definition: QueueProducerDefinition,
                         private val producer: QueueProducer,
                         private val future: SendResultFuture) : GoblinEvent() {

}