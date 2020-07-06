package org.goblinframework.queue.consumer.runner

import org.goblinframework.core.container.ContainerManagedBean
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.NamedDaemonThreadFactory
import org.goblinframework.queue.consumer.ConsumerRecordListener
import org.goblinframework.queue.consumer.QueueConsumerEvent
import java.util.concurrent.Semaphore
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@GoblinManagedBean("Queue")
abstract class AbstractListenerExecutors constructor(private val bean: ContainerManagedBean,
                                                     private val semaphore: Semaphore,
                                                     maxPermits: Int)
  : GoblinManagedObject(), ListenerExecutors, ListenerExecutorsMXBean {

  private val executor: ThreadPoolExecutor

  init {
    // 和10 max一下，避免因为worker并发数大于线程池数从而导致的RejectedExecutionException。
    // 这是因为信号量释放的时候线程还处于占用状态，当worker并发过大时会有可能导致线程池线程占满
    val maximumPoolSize = (maxPermits * 2).coerceAtLeast(10)

    this.executor = ThreadPoolExecutor(0, maximumPoolSize,
        60, TimeUnit.SECONDS,
        SynchronousQueue(),
        NamedDaemonThreadFactory.getInstance("QueueConsumerRunner"))
  }

  fun onEvent(event: QueueConsumerEvent) {
    event.recordListeners.forEach(ConsumerRecordListener::onHandled)
    executor.submit {
      try {
        val data = transform(event)
        event.recordListeners.forEach(ConsumerRecordListener::onTransformed)
        execute(data)
        event.recordListeners.forEach(ConsumerRecordListener::onSuccess)
      } catch (e: Exception) {
        event.recordListeners.forEach(ConsumerRecordListener::onFailure)
        logger.error("Failed to handle queue consumer event: [bean=${bean.beanName}, data=${event.data}]", e)
      } finally {
        semaphore.release()
      }
    }
  }

  override fun disposeBean() {
    try {
      executor.shutdown()
      executor.awaitTermination(5, TimeUnit.SECONDS)
    } catch (ignored: InterruptedException) {

    }
  }

  abstract fun transform(event: QueueConsumerEvent): Any?

  abstract fun execute(data: Any?)

  override fun getPoolSize(): Int {
    return executor.poolSize
  }

  override fun getActiveCount(): Int {
    return executor.activeCount
  }

  override fun getCorePoolSize(): Int {
    return executor.corePoolSize
  }

  override fun getMaximumPoolSize(): Int {
    return executor.maximumPoolSize
  }

  override fun getLargestPoolSize(): Int {
    return executor.largestPoolSize
  }

  override fun getTaskCount(): Long {
    return executor.taskCount
  }

  override fun getCompletedTaskCount(): Long {
    return executor.completedTaskCount
  }

  override fun getShutdown(): Boolean {
    return executor.isShutdown
  }

  override fun getTerminated(): Boolean {
    return executor.isTerminated
  }

  override fun getTerminating(): Boolean {
    return executor.isTerminating
  }
}