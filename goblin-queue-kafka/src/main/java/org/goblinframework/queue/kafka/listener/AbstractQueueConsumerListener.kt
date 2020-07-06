package org.goblinframework.queue.kafka.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.utils.Bytes
import org.goblinframework.core.event.EventBus
import org.goblinframework.queue.GoblinQueueException
import org.goblinframework.queue.consumer.ConsumerRecordListener
import org.goblinframework.queue.consumer.QueueConsumerEvent
import org.goblinframework.queue.consumer.runner.QueueListenerExecutors
import org.goblinframework.queue.module.QueueChannelManager
import java.util.concurrent.Semaphore

open class AbstractQueueConsumerListener(
    protected val semaphore: Semaphore,
    protected val recordListeners: List<ConsumerRecordListener>,
    protected val executors: QueueListenerExecutors
) {

  fun internalOnMessage(data: ConsumerRecord<Int, Bytes>) {
    recordListeners.forEach(ConsumerRecordListener::onFetched)

    val bytes = data.value().get()

    try {
      semaphore.acquire()
    } catch (ex: InterruptedException) {
      recordListeners.forEach(ConsumerRecordListener::onDiscarded);
      throw GoblinQueueException("Failed to acquire semaphore", ex)
    }

    val event = QueueConsumerEvent(bytes, this.recordListeners, executors)
    EventBus.publish(QueueChannelManager.CONSUMER_CHANNEL, event)
    recordListeners.forEach(ConsumerRecordListener::onPublished)
  }
}