package org.goblinframework.queue.rabbitmq.producer

import org.goblinframework.queue.rabbitmq.GoblinQueue

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinQueueProducer (

    val queues: Array<GoblinQueue>,

    val enable: Boolean = true
)