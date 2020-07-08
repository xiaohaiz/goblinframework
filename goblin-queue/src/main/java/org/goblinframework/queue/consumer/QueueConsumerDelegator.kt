package org.goblinframework.queue.consumer

import org.goblinframework.api.function.Disposable
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.api.QueueConsumerMXBean

@GoblinManagedBean(type = "Queue")
internal class QueueConsumerDelegator
internal constructor(private val delegator: QueueConsumer)
  : GoblinManagedObject(), QueueConsumer by delegator, QueueConsumerMXBean {

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
    logger.debug("Queue consumer [$location] disposed")
  }

  override fun getConsumerType(): String {
    return (delegator as QueueConsumerMXBean).consumerType
  }

  override fun getLocation(): String {
    return (delegator as QueueConsumerMXBean).location
  }

  override fun getMaxConcurrentConsumers(): Int {
    return (delegator as QueueConsumerMXBean).maxConcurrentConsumers
  }

  override fun getMaxPermits(): Int {
    return (delegator as QueueConsumerMXBean).maxPermits
  }

  override fun getGroup(): String {
    return (delegator as QueueConsumerMXBean).group
  }

  override fun getFetched(): Long {
    return (delegator as QueueConsumerMXBean).fetched
  }

  override fun getTransformed(): Long {
    return (delegator as QueueConsumerMXBean).transformed
  }

  override fun getHandled(): Long {
    return (delegator as QueueConsumerMXBean).handled
  }

  override fun getPublished(): Long {
    return (delegator as QueueConsumerMXBean).published
  }

  override fun getFailure(): Long {
    return (delegator as QueueConsumerMXBean).failure
  }

  override fun getDiscarded(): Long {
    return (delegator as QueueConsumerMXBean).discarded
  }

  override fun getReceived(): Long {
    return (delegator as QueueConsumerMXBean).received
  }

  override fun getSuccess(): Long {
    return (delegator as QueueConsumerMXBean).success
  }

}