package org.goblinframework.queue.producer

import org.goblinframework.queue.GoblinMessage

/**
 * high level api for sending GoblinMessage
 */
interface GoblinMessageProducer : QueueProducer {
    fun send(message: GoblinMessage)
}