package org.goblinframework.queue.rabbitmq.consumer

import org.goblinframework.queue.rabbitmq.GoblinQueue

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinQueueConsumer (

    val queues: Array<GoblinQueue>,

    val enable: Boolean = true
)