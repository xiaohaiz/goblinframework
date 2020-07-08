package org.goblinframework.queue.producer.builder

import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueProducer
import org.goblinframework.queue.producer.QueueProducerDefinition

interface QueueProducerBuilder {

    fun system(): QueueSystem

    fun producer(definition: QueueProducerDefinition): QueueProducer?
}