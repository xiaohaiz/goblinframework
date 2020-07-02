package org.goblinframework.queue.producer

import org.goblinframework.core.util.GoblinField
import org.goblinframework.queue.QueueLocation
import org.goblinframework.queue.api.GoblinQueueProducers

class QueueProducerDefinitionBuilder {

    companion object {
        fun build(field: GoblinField): List<QueueProducerDefinition> {
            val definitions = mutableListOf<QueueProducerDefinition>()

            if (field.isAnnotationPresent(GoblinQueueProducers::class.java)) {
                val annotations = field.findAnnotationSetterFirst(GoblinQueueProducers::class.java)
                annotations?.value?.forEach {
                    definitions.add(QueueProducerDefinition(QueueLocation(it.system, it.queue, it.config)))
                }
            }
            return definitions
        }
    }
}