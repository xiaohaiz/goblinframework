package org.goblinframework.queue.producer

import org.goblinframework.api.function.Disposable
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.api.QueueProducer
import org.goblinframework.queue.api.QueueProducerMXBean

@GoblinManagedBean(type = "Queue")
internal class QueueProducerDelegator
internal constructor(private val delegator: QueueProducer)
  : GoblinManagedObject(), QueueProducer by delegator, QueueProducerMXBean {

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
    logger.debug("Queue disposed")
  }
}