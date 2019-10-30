package org.goblinframework.queue.kafka.producer

import org.goblinframework.queue.kafka.QueueSystem

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class GoblinQueueProducer (

    val queue: String,             // queue name

    val queueSystem: QueueSystem = QueueSystem.RMQ,  // queue system type

    val name: String,

    val enable: Boolean = true
)