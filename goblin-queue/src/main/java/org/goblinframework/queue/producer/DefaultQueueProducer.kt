package org.goblinframework.queue.producer

import org.goblinframework.core.event.EventBus
import org.goblinframework.queue.SendResultFuture
import org.goblinframework.queue.api.QueueProducer

class DefaultQueueProducer(private val producerTuples: List<QueueProducerTuple>) : QueueProducer {
  override fun send(data: ByteArray) {
    sendAsync(data).awaitUninterruptibly()
  }

  override fun sendAsync(data: ByteArray): SendResultFuture {
    val result = SendResultFuture(producerTuples.size)

    producerTuples.forEach {
      val event = QueueProducerEvent(it.definition, it.producer, data, result)
      val future = EventBus.publish(event)
      future.addDiscardListener {
        // todo, local storage
      }
    }

    return result
  }
}