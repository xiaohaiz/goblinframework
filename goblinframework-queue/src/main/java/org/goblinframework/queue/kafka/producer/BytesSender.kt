package org.goblinframework.queue.kafka.producer

interface BytesSender: DataSender {
    fun send(data: Array<Byte>)
}