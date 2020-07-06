package org.goblinframework.queue.consumer

import org.goblinframework.core.event.GoblinEvent

class QueueConsumerEvent(
    val data: ByteArray,
    val recordListeners: List<ConsumerRecordListener>
) : GoblinEvent()