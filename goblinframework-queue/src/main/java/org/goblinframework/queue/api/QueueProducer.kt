package org.goblinframework.queue.api

/**
 * low level api for sending bytes directly
 */
interface QueueProducer {
    fun send(data: ByteArray)
}