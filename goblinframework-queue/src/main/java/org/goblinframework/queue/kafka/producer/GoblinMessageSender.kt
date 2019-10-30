package org.goblinframework.queue.kafka.producer

import org.goblinframework.queue.kafka.GoblinMessage

interface GoblinMessageSender : DataSender {
    fun send(message: GoblinMessage)
}