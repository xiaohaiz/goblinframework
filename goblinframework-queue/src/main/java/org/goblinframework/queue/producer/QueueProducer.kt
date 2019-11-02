package org.goblinframework.queue.producer

/**
 * low level api for sending bytes directly
 */
interface QueueProducer {
    fun send(data: ByteArray)
}