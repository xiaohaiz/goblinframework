package org.goblinframework.queue.kafka.producer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.queue.QueueLocation
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueProducer
import org.goblinframework.queue.producer.QueueProducerBuilder
import org.goblinframework.queue.producer.QueueProducerDefinition

@Singleton
class KafkaQueueProducerBuilder private constructor() : QueueProducerBuilder {

    companion object {
        @JvmField val INSTANCE = KafkaQueueProducerBuilder()
    }

    override fun system(): QueueSystem {
        return QueueSystem.KFK
    }

    override fun producer(definition: QueueProducerDefinition): QueueProducer {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}