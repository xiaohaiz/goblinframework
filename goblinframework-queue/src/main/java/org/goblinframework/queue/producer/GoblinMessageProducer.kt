package org.goblinframework.queue.producer

import org.goblinframework.queue.GoblinMessage

interface GoblinMessageProducer : QueueProducer {
    fun send(message: GoblinMessage)
}