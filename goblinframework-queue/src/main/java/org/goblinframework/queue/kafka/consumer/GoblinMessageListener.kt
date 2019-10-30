package org.goblinframework.queue.kafka.consumer

import org.goblinframework.queue.kafka.GoblinMessage

interface GoblinMessageListener {

    fun handle(message: GoblinMessage)
}