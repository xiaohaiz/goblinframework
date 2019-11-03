package org.goblinframework.queue.producer

import org.goblinframework.queue.QueueLocation
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueProducer

interface QueueProducerBuilder {

    fun system(): QueueSystem

    fun producer(location: QueueLocation): QueueProducer
}