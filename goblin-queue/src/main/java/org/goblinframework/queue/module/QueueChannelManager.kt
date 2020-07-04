package org.goblinframework.queue.module

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.EventBus
import org.goblinframework.queue.producer.QueueProducerEventListener

@Singleton
class QueueChannelManager private constructor() {

  companion object {
    @JvmField val INSTANCE = QueueChannelManager()

    const val PRODUCER_CHANNEL = "/goblin/queue/producer"
    const val CONSUMER_CHANNEL = "/goblin/queue/consumer"

    private const val PRODUCER_CHANNEL_SIZE = 32768
    private const val CONSUMER_CHANNEL_SIZE = 32768

    private const val PRODUCER_WORKER_NUM = 4
    private const val CONSUMER_WORKER_NUM = 4

  }

  private val producerEventListener = QueueProducerEventListener()

  fun initialize() {
    EventBus.register(PRODUCER_CHANNEL, PRODUCER_CHANNEL_SIZE, PRODUCER_WORKER_NUM)
    EventBus.register(CONSUMER_CHANNEL, CONSUMER_CHANNEL_SIZE, CONSUMER_WORKER_NUM)
    EventBus.subscribe(PRODUCER_CHANNEL, producerEventListener)
  }

  fun shutdown() {
    EventBus.unsubscribe(producerEventListener)
    EventBus.unregister(PRODUCER_CHANNEL)
    EventBus.unregister(CONSUMER_CHANNEL)
  }
}