package org.goblinframework.queue.kafka.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.utils.Bytes
import java.util.concurrent.Semaphore

open class AbstractQueueConsumerListener(
    protected val semaphore: Semaphore,
    protected val recordListeners: List<ConsumerRecordListener>
) {

  fun internalOnMessage(data: ConsumerRecord<Int, Bytes>) {
    recordListeners.forEach(ConsumerRecordListener::onFetched)


  }
}