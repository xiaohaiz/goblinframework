package org.goblinframework.queue.consumer

interface GoblinMessageListener {

    fun handle(message: GoblinMessage)
}