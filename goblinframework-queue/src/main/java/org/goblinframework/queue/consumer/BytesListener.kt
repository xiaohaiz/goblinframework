package org.goblinframework.queue.consumer

interface BytesListener : DataListener {

    fun handle(data: Array<Byte>)
}