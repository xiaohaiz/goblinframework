package org.goblinframework.queue.rabbitmq.producer

import org.goblinframework.queue.rabbitmq.GoblinMessage

interface GoblinMessageSender : DataSender {
    fun send(message: GoblinMessage)
}