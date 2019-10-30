package org.goblinframework.queue.kafka.consumer

interface BytesListener : DataListener {

    fun handle(data: Array<Byte>)
}