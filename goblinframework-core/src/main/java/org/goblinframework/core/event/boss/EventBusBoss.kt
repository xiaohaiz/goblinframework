package org.goblinframework.core.event.boss

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.api.event.*
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.event.context.GoblinEventContextImpl
import org.goblinframework.core.event.exception.BossRingBufferFullException
import org.goblinframework.core.event.worker.EventBusConfig
import org.goblinframework.core.event.worker.EventBusWorker
import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.core.util.NamedDaemonThreadFactory
import org.goblinframework.core.util.StopWatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.LongAdder
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "Core")
class EventBusBoss private constructor() : GoblinManagedObject(), EventBusBossMXBean {

  companion object {
    private const val DEFAULT_RING_BUFFER_SIZE = 65536
    private const val DEFAULT_WORK_HANDLER_NUMBER = 4
    private const val DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS = 15

    @JvmField val INSTANCE = EventBusBoss()
  }

  private val watch = StopWatch()
  private val disruptor: Disruptor<EventBusBossEvent>
  private val lock = ReentrantReadWriteLock()
  private val workers = mutableMapOf<String, EventBusWorker>()

  private val publishedCount = LongAdder()
  private val discardedCount = LongAdder()

  init {
    val threadFactory = NamedDaemonThreadFactory.getInstance("EventBusBoss")
    val eventFactory = EventBusBossEventFactory.INSTANCE
    disruptor = Disruptor<EventBusBossEvent>(eventFactory, DEFAULT_RING_BUFFER_SIZE, threadFactory)
    val handlers = Array(DEFAULT_WORK_HANDLER_NUMBER) { EventBusBossEventHandler.INSTANCE }
    disruptor.handleEventsWithWorkerPool(*handlers)
    disruptor.start()
  }

  fun register(channel: String, ringBufferSize: Int, workerHandlers: Int) {
    if (channel.isBlank()) throw GoblinEventException("Channel must not be blank")
    return register(EventBusConfig(channel, ringBufferSize, workerHandlers))
  }

  fun register(config: EventBusConfig) {
    lock.write {
      if (workers[config.channel] != null) {
        throw GoblinEventException("Channel [${config.channel}] already registered")
      }
      workers[config.channel] = EventBusWorker(config)
    }
  }

  fun unregister(channel: String) {
    lock.write { workers.remove(channel) }?.dispose()
  }

  fun subscribe(listener: GoblinEventListener) {
    AnnotationUtils.getAnnotation(listener.javaClass, GoblinEventChannel::class.java)?.run {
      subscribe(this.value, listener)
    } ?: throw GoblinEventException("No @GoblinEventChannel presented on [$listener]")
  }

  fun subscribe(channel: String, listener: GoblinEventListener) {
    if (channel.isBlank()) throw GoblinEventException("Channel must not be blank")
    val worker = lock.read { workers[channel] } ?: throw GoblinEventException("Channel [$channel] not found")
    worker.subscribe(listener)
  }

  fun unsubscribe(listener: GoblinEventListener) {
    AnnotationUtils.getAnnotation(listener.javaClass, GoblinEventChannel::class.java)?.run {
      unsubscribe(this.value, listener)
    } ?: throw GoblinEventException("No @GoblinEventChannel presented on [$listener]")
  }

  fun unsubscribe(channel: String, listener: GoblinEventListener) {
    if (channel.isBlank()) throw GoblinEventException("Channel must not be blank")
    val worker = lock.read { workers[channel] } ?: throw GoblinEventException("Channel [$channel] not found")
    worker.unsubscribe(listener)
  }

  fun publish(event: GoblinEvent): GoblinEventFuture {
    AnnotationUtils.getAnnotation(event.javaClass, GoblinEventChannel::class.java)?.run {
      return publish(this.value, event)
    } ?: throw GoblinEventException("No @GoblinEventChannel presented on [$event]")
  }

  fun publish(channel: String, event: GoblinEvent): GoblinEventFuture {
    if (channel.isBlank()) throw GoblinEventException("Channel must not be blank")
    val ctx = GoblinEventContextImpl(channel, event, GoblinEventFuture())
    val published = disruptor.ringBuffer.tryPublishEvent { e, _ -> e.ctx = ctx }
    if (!published) {
      discardedCount.increment()
      ctx.exceptionCaught(BossRingBufferFullException())
      ctx.complete()
    } else {
      publishedCount.increment()
    }
    return ctx.future()
  }

  internal fun lookup(channel: String): EventBusWorker? {
    return lock.read { workers[channel] }
  }

  override fun disposeBean() {
    try {
      disruptor.shutdown(DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
    } catch (ignore: TimeoutException) {
    }
    lock.write {
      workers.values.reversed().forEach { it.dispose() }
      workers.clear()
    }
    watch.stop()
  }

  override fun getUpTime(): String {
    return watch.toString()
  }

  override fun getBufferSize(): Int {
    return DEFAULT_RING_BUFFER_SIZE
  }

  override fun getRemainingCapacity(): Int {
    return disruptor.ringBuffer.remainingCapacity().toInt()
  }

  override fun getWorkers(): Int {
    return DEFAULT_WORK_HANDLER_NUMBER
  }

  override fun getPublishedCount(): Long {
    return publishedCount.sum()
  }

  override fun getDiscardedCount(): Long {
    return discardedCount.sum()
  }
}