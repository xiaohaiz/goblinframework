package org.goblinframework.queue.producer

import org.goblinframework.queue.api.QueueProducer

class QueueProducerTuple constructor(private val producers: List<QueueProducer>)
  : QueueProducer {
  override fun send(data: ByteArray?) {
    producers.forEach { it.send(data) }
  }

}