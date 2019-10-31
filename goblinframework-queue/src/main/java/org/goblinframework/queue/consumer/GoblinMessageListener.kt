package org.goblinframework.queue.consumer

import org.goblinframework.queue.GoblinMessage

interface GoblinMessageListener {

    fun handle(message: GoblinMessage)
}