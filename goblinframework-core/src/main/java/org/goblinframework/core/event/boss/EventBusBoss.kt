package org.goblinframework.core.event.boss

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.event.config.EventBusConfigLoader
import org.goblinframework.core.event.context.GoblinEventContextImpl
import org.goblinframework.core.event.context.GoblinEventFutureImpl
import org.goblinframework.core.event.exception.EventBossBufferFullException
import org.goblinframework.core.event.worker.EventBusWorker
import org.goblinframework.core.event.worker.EventBusWorkerMXBean
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.service.GoblinManagedStopWatch
import org.goblinframework.core.service.ServiceInstaller
import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.core.util.NamedDaemonThreadFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.LongAdder
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "Core")
@GoblinManagedStopWatch
class EventBusBoss private constructor() : GoblinManagedObject(), EventBusBossMXBean {

  companion object {
    private const val DEFAULT_RING_BUFFER_SIZE = 65536
    private const val DEFAULT_WORK_HANDLER_NUMBER = 4
    private const val DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS = 15

    @JvmField val INSTANCE = EventBusBoss()
  }

  private val disruptor: Disruptor<EventBusBossEvent>
  private val lock = ReentrantReadWriteLock()
  private val workers = mutableMapOf<String, EventBusWorker>()

  private val publishedCount = LongAdder()
  private val discardedCount = LongAdder()
  private val receivedCount = LongAdder()
  private val workerMissedCount = LongAdder()
  private val listenerMissedCount = LongAdder()
  private val dispatchedCount = LongAdder()

  init {
    val threadFactory = NamedDaemonThreadFactory.getInstance("EventBusBoss")
    val eventFactory = EventBusBossEventFactory.INSTANCE
    disruptor = Disruptor<EventBusBossEvent>(eventFactory, DEFAULT_RING_BUFFER_SIZE, threadFactory)
    val handlers = Array(DEFAULT_WORK_HANDLER_NUMBER) { EventBusBossEventHandler.INSTANCE }
    disruptor.handleEventsWithWorkerPool(*handlers)
    disruptor.start()
  }

  override fun initializeBean() {
    EventBusConfigLoader.INSTANCE.getChannelConfigs().forEach {
      register(it.channel, it.ringBufferSize, it.workerHandlers)
    }
    ServiceInstaller.asList(GoblinEventListener::class.java).forEach {
      subscribe(it)
    }
  }

  fun register(channel: String, ringBufferSize: Int, workerHandlers: Int) {
    if (channel.isBlank()) throw IllegalArgumentException("Channel must not be blank")
    lock.write {
      if (workers[channel] != null) {
        throw IllegalArgumentException("Channel [$channel] already registered")
      }
      workers[channel] = EventBusWorker(channel, ringBufferSize, workerHandlers)
    }
  }

  fun unregister(channel: String) {
    lock.write { workers.remove(channel) }?.dispose()
  }

  fun subscribe(listener: GoblinEventListener) {
    AnnotationUtils.getAnnotation(listener.javaClass, GoblinEventChannel::class.java)?.run {
      subscribe(this.value, listener)
    } ?: throw IllegalArgumentException("No @GoblinEventChannel presented on [$listener]")
  }

  fun subscribe(channel: String, listener: GoblinEventListener) {
    if (channel.isBlank()) throw IllegalArgumentException("Channel must not be blank")
    val worker = lock.read { workers[channel] } ?: throw IllegalArgumentException("Channel [$channel] not found")
    worker.subscribe(listener)
  }

  fun unsubscribe(listener: GoblinEventListener) {
    AnnotationUtils.getAnnotation(listener.javaClass, GoblinEventChannel::class.java)?.run {
      unsubscribe(this.value, listener)
    } ?: throw IllegalArgumentException("No @GoblinEventChannel presented on [$listener]")
  }

  fun unsubscribe(channel: String, listener: GoblinEventListener) {
    if (channel.isBlank()) throw IllegalArgumentException("Channel must not be blank")
    val worker = lock.read { workers[channel] } ?: throw IllegalArgumentException("Channel [$channel] not found")
    worker.unsubscribe(listener)
  }

  fun publish(event: GoblinEvent): GoblinEventFutureImpl {
    AnnotationUtils.getAnnotation(event.javaClass, GoblinEventChannel::class.java)?.run {
      return publish(this.value, event)
    } ?: throw IllegalArgumentException("No @GoblinEventChannel presented on [$event]")
  }

  fun publish(channel: String, event: GoblinEvent): GoblinEventFutureImpl {
    if (channel.isBlank()) throw IllegalArgumentException("Channel must not be blank")
    val ctx = GoblinEventContextImpl(channel, event, GoblinEventFutureImpl())
    val published = disruptor.ringBuffer.tryPublishEvent { e, _ ->
      e.ctx = ctx
      e.receivedCount = receivedCount
      e.workerMissedCount = workerMissedCount
      e.listenerMissedCount = listenerMissedCount
      e.dispatchedCount = dispatchedCount
    }
    if (!published) {
      discardedCount.increment()
      ctx.bossExceptionCaught(EventBossBufferFullException())
      ctx.complete()
    } else {
      publishedCount.increment()
    }
    return ctx.future()
  }

  internal fun lookup(channel: String): EventBusWorker? {
    return lock.read { workers[channel] }
  }

  override fun getUpTime(): String? {
    return stopWatch?.toString()
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

  override fun getReceivedCount(): Long {
    return receivedCount.sum()
  }

  override fun getWorkerMissedCount(): Long {
    return workerMissedCount.sum()
  }

  override fun getListenerMissedCount(): Long {
    return listenerMissedCount.sum()
  }

  override fun getDispatchedCount(): Long {
    return dispatchedCount.sum()
  }

  override fun getEventBusWorkerList(): Array<EventBusWorkerMXBean> {
    return lock.read { workers.values.toTypedArray() }
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
  }
}