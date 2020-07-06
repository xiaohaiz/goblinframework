package org.goblinframework.queue.consumer.runner

import org.goblinframework.queue.consumer.QueueConsumerEvent

interface ListenerExecutors {
  fun execute(event: QueueConsumerEvent)
}