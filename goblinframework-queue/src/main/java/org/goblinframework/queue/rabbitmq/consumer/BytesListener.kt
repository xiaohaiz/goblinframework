package org.goblinframework.queue.rabbitmq.consumer

interface BytesListener : DataListener {

    fun handle(data: Array<Byte>)
}