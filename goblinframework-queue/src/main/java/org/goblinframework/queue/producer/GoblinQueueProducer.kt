package org.goblinframework.queue.producer

import org.goblinframework.queue.QueueSystem

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Repeatable
annotation class GoblinQueueProducer (

        val queue: String,             // queue name

        val queueSystem: QueueSystem = QueueSystem.KFK,  // queue system type

        val config: String,

        val enable: Boolean = true
)