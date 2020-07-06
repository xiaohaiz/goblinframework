package org.goblinframework.queue.kafka.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.utils.Bytes
import org.goblinframework.queue.consumer.ConsumerRecordListener
import org.goblinframework.queue.consumer.runner.ListenerExecutors
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import java.util.concurrent.Semaphore

class AcknowledgeKafkaQueueConsumerListener(semaphore: Semaphore,
                                            recordListeners: List<ConsumerRecordListener>,
                                            executors: ListenerExecutors)
  : AbstractKafkaQueueConsumerListener(semaphore, recordListeners, executors), AcknowledgingMessageListener<Int, Bytes> {

  override fun onMessage(record: ConsumerRecord<Int, Bytes>?, acknowledgment: Acknowledgment) {
    if (record == null) return
    internalOnMessage(record)
    acknowledgment.acknowledge()
  }
}