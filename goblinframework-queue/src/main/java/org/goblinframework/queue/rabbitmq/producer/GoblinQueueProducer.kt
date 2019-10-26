package org.goblinframework.queue.rabbitmq.producer

import org.goblinframework.queue.rabbitmq.QueueSystem

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinQueueProducer (

    val queue: String,             // queue name

    val queueSystem: QueueSystem = QueueSystem.RMQ,  // queue system type

    val name: String,

    val enable: Boolean = true
)