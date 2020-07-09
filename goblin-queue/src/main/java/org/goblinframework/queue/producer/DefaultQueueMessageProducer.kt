package org.goblinframework.queue.producer

import org.goblinframework.core.event.EventBus
import org.goblinframework.queue.GoblinMessage
import org.goblinframework.queue.GoblinQueueException
import org.goblinframework.queue.SendResultFuture
import org.goblinframework.queue.api.QueueMessageProducer
import org.goblinframework.queue.module.QueueChannelManager
import org.goblinframework.queue.module.monitor.PMG
import org.goblinframework.queue.utils.QueueMessageEncoder

class DefaultQueueMessageProducer(private val producerTuples: List<QueueProducerTuple>) : QueueMessageProducer {
  override fun send(message: GoblinMessage) {
    sendAsync(message).awaitUninterruptibly()
  }

  override fun sendAsync(message: GoblinMessage): SendResultFuture {
    val result = SendResultFuture(producerTuples.size)

    producerTuples.forEach {
      val data = QueueMessageEncoder.encode(message, it.definition.serializer)
      if (data == null) {
        result.complete(1, GoblinQueueException("Failed to encode message [$message]"))
        return result
      }

      val instruction = PMG(it.definition.location)
      val event = QueueProducerEvent(it.definition, it.producer, data, result, instruction)
      val future = EventBus.publish(QueueChannelManager.PRODUCER_CHANNEL, event)
      future.addDiscardListener {
        instruction.close()
        result.complete(1, GoblinQueueException("Queue producer channel is full, message discarded"))
      }
    }

    return result
  }
}