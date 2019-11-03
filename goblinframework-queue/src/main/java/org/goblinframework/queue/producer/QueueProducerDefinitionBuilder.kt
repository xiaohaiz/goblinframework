package org.goblinframework.queue.producer

import org.goblinframework.core.util.GoblinField
import org.goblinframework.queue.QueueLocation

class QueueProducerDefinitionBuilder {

    companion object {
        fun build(field: GoblinField): List<QueueLocation> {
            val locations = mutableListOf<QueueLocation>()

            return locations
//            val definitionList = ArrayList<QueueProducerDefinition>()
//
//            val annotation = FieldAccessorUtils.findAnnotationSetterFirst(field, AlpsQueueProducer::class.java)
//            if (annotation != null) {
//                if (!annotation!!.enabled()) {
//                    return definitionList
//                }
//                val location = QueueLocation.newInstance(annotation!!.system(), annotation!!.config(), annotation!!.queue())
//                val definition = QueueProducerDefinition()
//                definition.setQueueLocation(location)
//                definition.setMessageEncodeMode(annotation!!.encodeMode())
//                definitionList.add(definition)
//                return definitionList
//            }
        }
    }
}