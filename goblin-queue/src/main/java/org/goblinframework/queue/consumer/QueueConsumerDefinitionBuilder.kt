package org.goblinframework.queue.consumer

import org.goblinframework.core.system.GoblinSystem
import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.queue.QueueLocation
import org.goblinframework.queue.api.GoblinConsumerMode
import org.goblinframework.queue.api.GoblinQueueConsumers

class QueueConsumerDefinitionBuilder {

  companion object {
    fun build(beanType: Class<*>?): Array<QueueConsumerDefinition?> {
      if (beanType == null) {
        return emptyArray()
      }

      val queueConsumers = AnnotationUtils.getAnnotation(beanType, GoblinQueueConsumers::class.java)

      val definitionMap = mutableMapOf<QueueLocation, QueueConsumerDefinition>()
      queueConsumers?.let {consumers ->
        val maxConcurrentConsumers = consumers.maxConcurrentConsumers
        val maxPermits = consumers.maxPermits


        consumers.consumers.forEach { consumer ->
          val location = QueueLocation(consumer.system, consumer.queue, consumer.config)
          var group = consumer.group
          if (group.isBlank()) {
            if (consumers.mode == GoblinConsumerMode.PUBSUB) {
              group = GoblinSystem.applicationName()
            } else if (consumers.mode == GoblinConsumerMode.QUEUE) {
              group = location.queue
            }
          }
          val definition = QueueConsumerDefinition(location, maxConcurrentConsumers, maxPermits, group)

          definitionMap[location] = definition
        }
      }

      return definitionMap.values.toTypedArray()
    }
  }
}