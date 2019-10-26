package org.goblinframework.queue.rabbitmq.producer

interface BytesSender: DataSender {
    fun send(data: Array<Byte>)
}