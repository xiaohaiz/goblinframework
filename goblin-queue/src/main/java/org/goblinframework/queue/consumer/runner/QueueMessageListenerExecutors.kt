package org.goblinframework.queue.consumer.runner

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.queue.GoblinMessage
import org.goblinframework.queue.api.QueueMessageListener
import org.goblinframework.queue.consumer.QueueConsumerEvent
import org.goblinframework.queue.utils.QueueMessageEncoder
import java.util.concurrent.Semaphore

class QueueMessageListenerExecutors(bean: ContainerManagedBean, semaphore: Semaphore, maxPermits: Int)
: AbstractListenerExecutors(bean, semaphore, maxPermits) {

  private val listener: QueueMessageListener = bean.getBean() as QueueMessageListener

  override fun transform(event: QueueConsumerEvent): Any? {
    return QueueMessageEncoder.decode(event.data)
  }

  override fun doExecute(data: Any?) {
    if (data == null) return
    listener.handle(data as GoblinMessage)
  }
}