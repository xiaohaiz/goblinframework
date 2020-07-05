package org.goblinframework.queue.consumer

import org.goblinframework.api.function.Disposable
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.api.QueueConsumerMXBean

@GoblinManagedBean(type = "Queue")
internal class QueueConsumerDelegator internal constructor(private val delegator: QueueConsumer)
  : GoblinManagedObject(), QueueConsumer by delegator, QueueConsumerMXBean {

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
    logger.debug("Queue consumer disposed")
  }
}