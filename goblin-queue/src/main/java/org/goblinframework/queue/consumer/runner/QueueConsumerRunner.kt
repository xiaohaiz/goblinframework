package org.goblinframework.queue.consumer.runner

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.queue.api.QueueListener
import org.goblinframework.queue.consumer.QueueConsumerEvent
import java.util.concurrent.Semaphore

class QueueConsumerRunner(bean: ContainerManagedBean, semaphore: Semaphore, maxPermits: Int)
  : AbstractQueueConsumerRunner(bean, semaphore, maxPermits) {

  private val listener: QueueListener = bean.getBean() as QueueListener

  override fun doOnEvent(event: QueueConsumerEvent) {
    listener.handle(event.data)
  }
}