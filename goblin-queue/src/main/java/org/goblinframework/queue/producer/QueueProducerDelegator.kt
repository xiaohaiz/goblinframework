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
    logger.debug("Queue producer disposed")
  }

  override fun getSuccessCount(): Long {
    return (delegator as QueueProducerMXBean).successCount
  }

  override fun getLocation(): String {
    return (delegator as QueueProducerMXBean).location
  }

  override fun getSerializer(): String {
    return (delegator as QueueProducerMXBean).serializer
  }

  override fun getProducerType(): String {
    return (delegator as QueueProducerMXBean).producerType
  }

  override fun getFailureCount(): Long {
    return (delegator as QueueProducerMXBean).failureCount
  }

  override fun produceText(text: String?) {
    (delegator as QueueProducerMXBean).produceText(text)
  }
}