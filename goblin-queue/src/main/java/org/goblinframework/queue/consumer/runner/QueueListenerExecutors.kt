package org.goblinframework.queue.consumer.runner

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.queue.api.QueueListener
import org.goblinframework.queue.consumer.QueueConsumerEvent
import java.util.concurrent.Semaphore

class QueueListenerExecutors(bean: ContainerManagedBean, semaphore: Semaphore, maxPermits: Int)
  : AbstractListenerExecutors(bean, semaphore, maxPermits) {

  private val listener: QueueListener = bean.getBean() as QueueListener

  override fun transform(event: QueueConsumerEvent): Any? {
    return event.data
  }

  override fun execute(data: Any?) {
    if (data == null) return
    listener.handle(data as ByteArray)
  }
}