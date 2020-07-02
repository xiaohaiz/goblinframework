package org.goblinframework.queue.consumer

import org.goblinframework.queue.GoblinQueue

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinQueueConsumer (

        val queues: Array<GoblinQueue>,

        val enable: Boolean = true
)