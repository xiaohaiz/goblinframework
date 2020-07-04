package org.goblinframework.queue.producer

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.queue.SendResultFuture
import org.goblinframework.queue.api.QueueProducer

class QueueProducerEvent(val definition: QueueProducerDefinition,
                         val producer: QueueProducer,
                         val data: ByteArray,
                         val future: SendResultFuture) : GoblinEvent() {

}