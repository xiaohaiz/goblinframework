package org.goblinframework.queue.producer

import org.goblinframework.queue.GoblinMessage

interface GoblinMessageSender : DataSender {
    fun send(message: GoblinMessage)
}