package org.goblinframework.queue.kafka.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.utils.Bytes
import org.goblinframework.queue.consumer.ConsumerRecordListener
import org.goblinframework.queue.consumer.runner.ListenerExecutors
import org.springframework.kafka.listener.MessageListener
import java.util.concurrent.Semaphore

class KafkaQueueConsumerListener(semaphore: Semaphore,
                                 recordListeners: List<ConsumerRecordListener>,
                                 executors: ListenerExecutors)
  : AbstractKafkaQueueConsumerListener(semaphore, recordListeners, executors), MessageListener<Int, Bytes> {
  override fun onMessage(record: ConsumerRecord<Int, Bytes>?) {
    if (record == null) return
    internalOnMessage(record)
  }
}