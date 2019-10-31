package org.goblinframework.queue.producer

interface BytesSender: DataSender {
    fun send(data: Array<Byte>)
}