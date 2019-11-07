package org.goblinframework.queue.producer

import org.goblinframework.queue.GoblinMessage
import org.goblinframework.queue.api.QueueMessageProducer
import org.goblinframework.queue.api.QueueProducer

class QueueMessageProducerTuple(private val producers: List<QueueProducer>) : QueueMessageProducer {
  override fun send(message: GoblinMessage?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}