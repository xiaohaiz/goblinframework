package org.goblinframework.queue.consumer

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.GoblinQueueException
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.api.QueueConsumerMXBean
import org.goblinframework.queue.api.QueueListener
import org.goblinframework.queue.api.QueueMessageListener
import org.goblinframework.queue.consumer.executor.ListenerExecutors
import org.goblinframework.queue.consumer.executor.QueueListenerExecutors
import org.goblinframework.queue.consumer.executor.QueueMessageListenerExecutors
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicLong

abstract class AbstractQueueConsumer
constructor(protected val definition: QueueConsumerDefinition,
            protected val bean: ContainerManagedBean)
  : GoblinManagedObject(), QueueConsumer, QueueConsumerMXBean {

  protected val semaphore: Semaphore = Semaphore(definition.maxPermits)
  protected val executors: ListenerExecutors

  protected val fetched = AtomicLong(0)
  protected val transformed = AtomicLong(0)
  protected val discarded = AtomicLong(0)
  protected val published = AtomicLong(0)
  protected val received = AtomicLong(0)
  protected val handled = AtomicLong(0)
  protected val success = AtomicLong(0)
  protected val failure = AtomicLong(0)

  protected val recordListeners = mutableListOf<ConsumerRecordListener>(
      object : ConsumerRecordListener {
        override fun onFetched() {
          fetched.incrementAndGet()
        }

        override fun onDiscarded() {
          discarded.incrementAndGet()
        }

        override fun onPublished() {
          published.incrementAndGet()
        }

        override fun onReceived() {
          received.incrementAndGet()
        }

        override fun onTransformed() {
          transformed.incrementAndGet()
        }

        override fun onHandled() {
          handled.incrementAndGet()
        }

        override fun onSuccess() {
          success.incrementAndGet()
        }

        override fun onFailure() {
          failure.incrementAndGet()
        }

      }
  )

  init {
    executors = when (bean.getBean()) {
      is QueueMessageListener -> {
        QueueMessageListenerExecutors(bean, semaphore, definition.maxPermits)
      }
      is QueueListener -> {
        QueueListenerExecutors(bean, semaphore, definition.maxPermits)
      }
      else -> {
        throw GoblinQueueException("Unrecognized bean type for bean {${bean.beanName}}, must be QueueMessageListener or QueueListener")
      }
    }
  }

  override fun disposeBean() {
    (executors as GoblinManagedObject).dispose()
  }

  override fun getLocation(): String {
    return definition.location.toString()
  }

  override fun getMaxConcurrentConsumers(): Int {
    return definition.maxConcurrentConsumers
  }

  override fun getMaxPermits(): Int {
    return definition.maxPermits
  }

  override fun getGroup(): String {
    return definition.group
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