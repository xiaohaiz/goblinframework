package org.goblinframework.queue.consumer

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.api.QueueConsumerMXBean
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicLong

open class AbstractQueueConsumer
constructor(protected val definition: QueueConsumerDefinition, protected val bean: ContainerManagedBean)
  : GoblinManagedObject(), QueueConsumer, QueueConsumerMXBean {

  private val semaphore: Semaphore = Semaphore(definition.maxPermits)

  private val fetched = AtomicLong(0)
  private val transformed = AtomicLong(0)
  private val discarded = AtomicLong(0)
  private val published = AtomicLong(0)
  private val received = AtomicLong(0)
  private val handled = AtomicLong(0)
  private val success = AtomicLong(0)
  private val failure = AtomicLong(0)

  override fun disposeBean() {
  }

  override fun getConnectionName(): String {
    return definition.location.config
  }

  override fun getQueueName(): String {
    return definition.location.queue
  }

  override fun getQueueSystem(): QueueSystem {
    return definition.location.queueSystem
  }

  override fun getFetched(): Long {
    return fetched.get()
  }

  override fun getTransformed(): Long {
    return transformed.get()
  }

  override fun getHandled(): Long {
    return handled.get()
  }

  override fun getPublished(): Long {
    return published.get()
  }

  override fun getFailure(): Long {
    return failure.get()
  }

  override fun getDiscarded(): Long {
    return discarded.get()
  }

  override fun getReceived(): Long {
    return received.get()
  }

  override fun getSuccess(): Long {
    return success.get()
  }
}