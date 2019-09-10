package org.goblinframework.core.event.boss

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.core.concurrent.NamedDaemonThreadFactory
import org.goblinframework.core.event.*
import org.goblinframework.core.event.config.EventBusConfig
import org.goblinframework.core.event.config.EventBusConfigLoader
import org.goblinframework.core.event.context.GoblinEventContextImpl
import org.goblinframework.core.event.dsl.GoblinCallbackEventListener
import org.goblinframework.core.event.exception.BossRingBufferFullException
import org.goblinframework.core.event.worker.EventBusWorker
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.core.util.ServiceInstaller
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean("CORE")
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
  private val closed = AtomicBoolean()

  init {
    val threadFactory = NamedDaemonThreadFactory.getInstance("EventBusBoss")
    val eventFactory = EventBusBossEventFactory.INSTANCE
    disruptor = Disruptor<EventBusBossEvent>(eventFactory, DEFAULT_RING_BUFFER_SIZE, threadFactory)
    val handlers = Array(DEFAULT_WORK_HANDLER_NUMBER) { EventBusBossEventHandler.INSTANCE }
    disruptor.handleEventsWithWorkerPool(*handlers)
    disruptor.start()

    EventBusConfigLoader.configs.forEach { register(it) }

    subscribe(GoblinCallbackEventListener.INSTANCE)
    ServiceInstaller.installedList(GoblinEventListener::class.java).forEach { subscribe(it) }
  }

  fun initialize() {}

  fun register(channel: String, ringBufferSize: Int, workerHandlers: Int) {
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
    lock.write { workers.remove(channel) }?.run { close() }
  }

  fun subscribe(listener: GoblinEventListener) {
    val annotation = AnnotationUtils.findAnnotation(listener.javaClass, GoblinEventChannel::class.java)
    if (annotation == null || annotation.value.isBlank()) {
      throw GoblinEventException("No available channel found from [$listener]")
    }
    subscribe(annotation.value, listener)
  }

  fun subscribe(channel: String, listener: GoblinEventListener) {
    val worker = lock.read { workers[channel] } ?: throw GoblinEventException("Channel [$channel] not found")
    worker.subscribe(listener)
  }

  fun unsubscribe(listener: GoblinEventListener) {
    val annotation = AnnotationUtils.findAnnotation(listener.javaClass, GoblinEventChannel::class.java)
    if (annotation == null || annotation.value.isBlank()) {
      throw GoblinEventException("No available channel found from [$listener]")
    }
    unsubscribe(annotation.value, listener)
  }

  fun unsubscribe(channel: String, listener: GoblinEventListener) {
    val worker = lock.read { workers[channel] } ?: throw GoblinEventException("Channel [$channel] not found")
    worker.unsubscribe(listener)
  }

  fun publish(event: GoblinEvent): GoblinEventFuture {
    val annotation = AnnotationUtils.findAnnotation(event.javaClass, GoblinEventChannel::class.java)
    if (annotation == null || annotation.value.isBlank()) {
      throw GoblinEventException("No available channel found from [$event]")
    }
    return publish(annotation.value, event)
  }

  fun publish(channel: String, event: GoblinEvent): GoblinEventFuture {
    val ctx = GoblinEventContextImpl(channel, event, GoblinEventFuture())
    val published = disruptor.ringBuffer.tryPublishEvent { e, _ -> e.ctx = ctx }
    if (!published) {
      ctx.exceptionCaught(BossRingBufferFullException())
      ctx.complete()
    }
    return ctx.future()
  }

  internal fun lookup(channel: String): EventBusWorker? {
    return lock.read { workers[channel] }
  }

  fun close() {
    if (!closed.compareAndSet(false, true)) {
      return
    }
    unregisterIfNecessary()
    try {
      disruptor.shutdown(DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
      EventBus.LOGGER.info("EventBusBoss closed")
    } catch (ex: TimeoutException) {
      EventBus.LOGGER.warn("EventBusBoss close timeout", ex)
    }
    lock.write {
      workers.values.reversed().forEach { it.close() }
      workers.clear()
    }
    EventBus.LOGGER.info("EventBus closed")
  }
}