package org.goblinframework.queue.consumer.runner

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.queue.api.QueueMessageListener
import org.goblinframework.queue.consumer.QueueConsumerEvent
import org.goblinframework.queue.utils.QueueMessageEncoder
import java.util.concurrent.Semaphore

class QueueMessageListenerExecutors(bean: ContainerManagedBean, semaphore: Semaphore, maxPermits: Int)
: AbstractListenerExecutors(bean, semaphore, maxPermits) {

  private val listener: QueueMessageListener = bean.getBean() as QueueMessageListener

  override fun doOnEvent(event: QueueConsumerEvent) {
    val message = QueueMessageEncoder.decode(event.data)
    listener.handle(message)
  }
}