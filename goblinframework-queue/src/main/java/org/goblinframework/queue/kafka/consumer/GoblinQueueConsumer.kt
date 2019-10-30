package org.goblinframework.queue.kafka.consumer

import org.goblinframework.queue.kafka.GoblinQueue

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinQueueConsumer (

    val queues: Array<GoblinQueue>,

    val enable: Boolean = true
)