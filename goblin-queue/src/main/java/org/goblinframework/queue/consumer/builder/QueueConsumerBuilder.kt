package org.goblinframework.queue.consumer.builder

import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.consumer.QueueConsumerDefinition

interface QueueConsumerBuilder {
  fun system(): QueueSystem

  fun consumer(queueConsumerDefinition: QueueConsumerDefinition): QueueConsumer
}