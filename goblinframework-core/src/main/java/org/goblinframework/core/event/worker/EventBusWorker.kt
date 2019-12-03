package org.goblinframework.core.event.worker

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.event.GoblinEventListenerImpl
import org.goblinframework.core.event.GoblinEventListenerMXBean
import org.goblinframework.core.event.context.GoblinEventContextImpl
import org.goblinframework.core.event.exception.EventWorkerBufferFullException
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.NamedDaemonThreadFactory
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.core.util.StopWatch
import org.goblinframework.core.util.SystemUtils
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.LongAdder
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "Core")
class EventBusWorker internal constructor(private val channel: String,
                                          private val bufferSize: Int,
                                          workers: Int)
  : GoblinManagedObject(), EventBusWorkerMXBean {

  companion object {
    private const val DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS = 15
  }

  private val id = RandomUtils.nextObjectId()
  private val watch = StopWatch()
  private val workers: Int
  private val disruptor: Disruptor<EventBusWorkerEvent>
  private val lock = ReentrantReadWriteLock()
  private val listeners = IdentityHashMap<GoblinEventListener, GoblinEventListenerImpl>()

  private val publishedCount = LongAdder()
  private val discardedCount = LongAdder()
  private val receivedCount = LongAdder()
  private val succeedCount = LongAdder()
  private val failedCount = LongAdder()

  init {
    val threadFactory = NamedDaemonThreadFactory.getInstance("EventBusWorker-$channel")
    val eventFactory = EventBusWorkerEventFactory.INSTANCE
    disruptor = Disruptor<EventBusWorkerEvent>(eventFactory, bufferSize, threadFactory)
    this.workers = if (workers <= 0) SystemUtils.estimateThreads() else workers
    val handlers = Array(this.workers) { EventBusWorkerEventHandler.INSTANCE }
    disruptor.handleEventsWithWorkerPool(*handlers)
    disruptor.start()
  }

  internal fun subscribe(listener: GoblinEventListener) {
    lock.write {
      if (listeners[listener] != null) {
        throw IllegalArgumentException("Listener [$listener] already subscribed on channel [$channel]")
      }
      listeners[listener] = GoblinEventListenerImpl(listener)
    }
  }

  internal fun unsubscribe(listener: GoblinEventListener) {
    lock.write { listeners.remove(listener) }?.dispose()
  }

  internal fun lookup(ctx: GoblinEventContext): List<GoblinEventListener> {
    return lock.read { listeners.values.filter { it.accept(ctx) }.toList() }
  }

  fun publish(taskId: Int, ctx: GoblinEventContextImpl, listeners: List<GoblinEventListener>) {
    val published = disruptor.ringBuffer.tryPublishEvent { e, _ ->
      e.taskId = taskId
      e.ctx = ctx
      e.listeners = listeners
      e.receivedCount = receivedCount
      e.succeedCount = succeedCount
      e.failedCount = failedCount
    }
    if (!published) {
      discardedCount.increment()
      ctx.workerExceptionCaught(taskId, EventWorkerBufferFullException())
      ctx.finishTask()
    } else {
      publishedCount.increment()
    }
  }

  override fun disposeBean() {
    lock.write {
      listeners.values.forEach { it.dispose() }
      listeners.clear()
    }
    try {
      disruptor.shutdown(DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
    } catch (ignore: TimeoutException) {
    }
    watch.stop()
  }

  override fun getId(): String {
    return id
  }

  override fun getUpTime(): String {
    return watch.toString()
  }

  override fun getChannel(): String {
    return channel
  }

  override fun getBufferSize(): Int {
    return bufferSize
  }

  override fun getRemainingCapacity(): Int {
    return disruptor.ringBuffer.remainingCapacity().toInt()
  }

  override fun getWorkers(): Int {
    return workers
  }

  override fun getPublishedCount(): Long {
    return publishedCount.sum()
  }

  override fun getDiscardedCount(): Long {
    return discardedCount.sum()
  }

  override fun getReceivedCount(): Long {
    return receivedCount.sum()
  }

  override fun getSucceedCount(): Long {
    return succeedCount.sum()
  }

  override fun getFailedCount(): Long {
    return failedCount.sum()
  }

  override fun getEventListenerList(): Array<GoblinEventListenerMXBean> {
    return lock.read { listeners.values.toTypedArray() }
  }
}