package org.goblinframework.queue.consumer

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.queue.consumer.executor.ListenerExecutors

class QueueConsumerEvent(
    val data: ByteArray,
    val recordListeners: List<ConsumerRecordListener>,
    val executors: ListenerExecutors
) : GoblinEvent()