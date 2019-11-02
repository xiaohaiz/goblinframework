package org.goblinframework.queue.producer

interface BytesProducer: QueueProducer {
    fun send(data: Array<Byte>)
}