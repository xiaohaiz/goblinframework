package org.goblinframework.queue.kafka.producer

import org.goblinframework.queue.SendResultFuture
import org.goblinframework.queue.api.QueueProducer
import org.goblinframework.queue.producer.QueueProducerDefinition

class KafkaQueueProducer : QueueProducer {

  private val definition: QueueProducerDefinition

  constructor(definition: QueueProducerDefinition) {
    this.definition = definition

  }

  override fun sendAsync(data: ByteArray?): SendResultFuture {
    TODO("Not yet implemented")
  }

  override fun send(data: ByteArray?) {
    TODO("Not yet implemented")
  }

}