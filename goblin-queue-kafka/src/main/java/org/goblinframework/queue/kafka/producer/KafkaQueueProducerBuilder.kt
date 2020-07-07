package org.goblinframework.queue.kafka.producer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueProducer
import org.goblinframework.queue.producer.QueueProducerDefinition
import org.goblinframework.queue.producer.builder.QueueProducerBuilder

@Singleton
class KafkaQueueProducerBuilder : GoblinManagedObject(), QueueProducerBuilder {

  companion object {
    @JvmField val INSTANCE = KafkaQueueProducerBuilder()
  }

  override fun system(): QueueSystem {
    return QueueSystem.KFK
  }

  override fun producer(definition: QueueProducerDefinition): QueueProducer? {
    return KafkaQueueProducer(definition)
  }
}