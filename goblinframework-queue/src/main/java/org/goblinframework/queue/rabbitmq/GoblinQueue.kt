package org.goblinframework.queue.rabbitmq

/**
 * queue definition annotation
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class GoblinQueue(val queue: String,             // queue name
                             val queueSystem: QueueSystem,  // queue system type
                             val name: String)              // queue config name
