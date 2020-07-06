package org.goblinframework.queue.kafka.consumer

import org.apache.kafka.common.utils.Bytes
import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.queue.GoblinQueueException
import org.goblinframework.queue.consumer.AbstractQueueConsumer
import org.goblinframework.queue.consumer.QueueConsumerDefinition
import org.goblinframework.queue.kafka.client.KafkaQueueConsumerClientManager
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties

@GoblinManagedBean(type = "Kafka", name = "KafkaQueueConsumer")
class KafkaQueueConsumer
constructor(definition: QueueConsumerDefinition, bean: ContainerManagedBean)
  : AbstractQueueConsumer(definition, bean) {

  private val container: ConcurrentMessageListenerContainer<Int, Bytes>

  init {
    val client = KafkaQueueConsumerClientManager.INSTANCE.getConsumerClient(definition.location.config, definition.group)
        ?: throw GoblinQueueException("Failed to get consumer client for $definition")

    val factory = client.consumerFactory()
    if (factory.isAutoCommit) {

    } else {

    }

    val properties = ContainerProperties(definition.location.queue)
    container = ConcurrentMessageListenerContainer(factory, properties)
    container.concurrency = definition.maxConcurrentConsumers
    container.beanName = definition.location.queue
  }
}