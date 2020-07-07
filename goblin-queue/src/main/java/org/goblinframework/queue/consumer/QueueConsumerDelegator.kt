package org.goblinframework.queue.consumer

import org.goblinframework.api.function.Disposable
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.api.QueueConsumerMXBean

@GoblinManagedBean(type = "Queue")
internal class QueueConsumerDelegator
internal constructor(private val delegator: QueueConsumer)
  : GoblinManagedObject(), QueueConsumer by delegator, QueueConsumerMXBean {

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
    logger.debug("Queue consumer disposed")
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

  override fun getConnectionName(): String {
    return (delegator as QueueConsumerMXBean).connectionName
  }

  override fun getQueueName(): String {
    return (delegator as QueueConsumerMXBean).queueName
  }

  override fun getQueueSystem(): QueueSystem {
    return (delegator as QueueConsumerMXBean).queueSystem
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