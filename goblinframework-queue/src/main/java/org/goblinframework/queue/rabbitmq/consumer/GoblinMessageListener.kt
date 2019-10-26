package org.goblinframework.queue.rabbitmq.consumer

import org.goblinframework.queue.rabbitmq.GoblinMessage

interface GoblinMessageListener {

    fun handle(message: GoblinMessage)
}