package org.goblinframework.core.event.worker

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventException
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.event.context.GoblinEventContextImpl
import org.goblinframework.core.event.exception.WorkerRingBufferFullException
import org.goblinframework.core.util.NamedDaemonThreadFactory
import org.goblinframework.core.util.StopWatch
import org.goblinframework.core.util.SystemUtils
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.LongAdder
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "core")
class EventBusWorker
internal constructor(private val channel: String,
                     private val bufferSize: Int,
                     workers: Int)
  : GoblinManagedObject(), EventBusWorkerMXBean {

  companion object {
    private const val DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS = 15
  }

  private val watch = StopWatch()
  private val workers: Int
  private val disruptor: Disruptor<EventBusWorkerEvent>
  private val lock = ReentrantReadWriteLock()
  private val listeners = IdentityHashMap<GoblinEventListener, Instant>()

  private val publishedCount = LongAdder()
  private val discardedCount = LongAdder()

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
        throw GoblinEventException("Listener [$listener] already subscribed on channel [$channel]")
      }
      listeners[listener] = Instant.now()
    }
  }

  internal fun unsubscribe(listener: GoblinEventListener) {
    lock.write {
      listeners.remove(listener)
    }
  }

  internal fun lookup(ctx: GoblinEventContext): List<GoblinEventListener> {
    return lock.read { listeners.keys.filter { it.accept(ctx) }.toList() }
  }

  internal fun public(ctx: GoblinEventContextImpl, listeners: List<GoblinEventListener>) {
    val published = disruptor.ringBuffer.tryPublishEvent { e, _ ->
      e.ctx = ctx
      e.listeners = listeners
    }
    if (!published) {
      discardedCount.increment()
      ctx.exceptionCaught(WorkerRingBufferFullException(ctx.channel))
      ctx.finishTask()
    } else {
      publishedCount.increment()
    }
  }

  override fun disposeBean() {
    try {
      disruptor.shutdown(DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
    } catch (ignore: TimeoutException) {
    }
    watch.stop()
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
}