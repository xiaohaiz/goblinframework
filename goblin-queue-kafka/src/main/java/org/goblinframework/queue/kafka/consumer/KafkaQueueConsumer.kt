package org.goblinframework.queue.kafka.consumer

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.queue.consumer.AbstractQueueConsumer
import org.goblinframework.queue.consumer.QueueConsumerDefinition

@GoblinManagedBean(type = "Kafka", name = "KafkaQueueConsumer")
class KafkaQueueConsumer
constructor(definition: QueueConsumerDefinition, bean: ContainerManagedBean)
  : AbstractQueueConsumer(definition, bean) {

  init {

  }
}