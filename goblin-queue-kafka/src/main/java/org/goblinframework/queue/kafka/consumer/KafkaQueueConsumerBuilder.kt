package org.goblinframework.queue.kafka.consumer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.consumer.QueueConsumerDefinition
import org.goblinframework.queue.consumer.builder.QueueConsumerBuilder

@Singleton
class KafkaQueueConsumerBuilder : QueueConsumerBuilder {

  companion object {
    @JvmField val INSTANCE = KafkaQueueConsumerBuilder()
  }

  override fun system(): QueueSystem {
    return QueueSystem.KFK
  }

  override fun consumer(definition: QueueConsumerDefinition, reference: Any): QueueConsumer? {
    val bean = reference as ContainerManagedBean
    return KafkaQueueConsumer(definition, bean)
  }

}