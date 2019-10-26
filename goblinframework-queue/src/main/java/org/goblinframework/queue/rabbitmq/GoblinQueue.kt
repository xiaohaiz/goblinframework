package org.goblinframework.queue.rabbitmq

import org.goblinframework.core.serialization.SerializerMode

/**
 * queue definition annotation
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinQueue (
    val queue: String,             // queue name

    val queueSystem: QueueSystem = QueueSystem.RMQ,  // queue system type

    val name: String,

    val encoder: SerializerMode = SerializerMode.HESSIAN2
)
