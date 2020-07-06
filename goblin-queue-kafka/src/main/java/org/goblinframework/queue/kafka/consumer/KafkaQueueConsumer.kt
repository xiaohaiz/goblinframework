package org.goblinframework.queue.kafka.consumer

import org.apache.kafka.common.utils.Bytes
import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.queue.GoblinQueueException
import org.goblinframework.queue.consumer.AbstractQueueConsumer
import org.goblinframework.queue.consumer.QueueConsumerDefinition
import org.goblinframework.queue.kafka.client.KafkaQueueConsumerClientManager
import org.goblinframework.queue.kafka.listener.AcknowledgeKafkaQueueConsumerListener
import org.goblinframework.queue.kafka.listener.KafkaQueueConsumerListener
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
    val properties = ContainerProperties(definition.location.queue)
    if (factory.isAutoCommit) {
      properties.messageListener = KafkaQueueConsumerListener(semaphore, recordListeners, executors)
    } else {
      properties.ackMode = ContainerProperties.AckMode.MANUAL
      properties.messageListener = AcknowledgeKafkaQueueConsumerListener(semaphore, recordListeners, executors)
    }


    container = ConcurrentMessageListenerContainer(factory, properties)
    container.concurrency = definition.maxConcurrentConsumers
    container.beanName = definition.location.queue
  }

  override fun start() {
    if (!container.isRunning) {
      container.start()
      logger.info("Kafka Queue Consumer $definition started")
    }
  }

  override fun stop() {
    if (container.isRunning) {
      container.stop()
      logger.info("Kafka Queue Consumer $definition stopped")
    }
  }
}