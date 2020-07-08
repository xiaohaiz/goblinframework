package org.goblinframework.queue.consumer.executor

import org.goblinframework.queue.consumer.QueueConsumerEvent

interface ListenerExecutors {
  fun execute(event: QueueConsumerEvent)

  fun shutdown()
}