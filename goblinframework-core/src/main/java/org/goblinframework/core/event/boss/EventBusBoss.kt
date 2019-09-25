package org.goblinframework.core.event.boss

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.api.event.*
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.api.service.ServiceInstaller
import org.goblinframework.core.event.callback.GoblinCallbackEventListener
import org.goblinframework.core.event.config.EventBusConfig
import org.goblinframework.core.event.config.EventBusConfigLoader
import org.goblinframework.core.event.context.GoblinEventContextImpl
import org.goblinframework.core.event.exception.BossRingBufferFullException
import org.goblinframework.core.event.timer.TimerEventGenerator
import org.goblinframework.core.event.worker.EventBusWorker
import org.goblinframework.core.system.SubModuleEventListener
import org.goblinframework.core.util.AnnotationUtils
import org.goblinframework.core.util.NamedDaemonThreadFactory
import java.util.concurrent.TimeUnit
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

  private val disruptor: Disruptor<EventBusBossEvent>
  private val lock = ReentrantReadWriteLock()
  private val workers = mutableMapOf<String, EventBusWorker>()
  private val timerEventGenerator = TimerEventGenerator()

  init {
    val threadFactory = NamedDaemonThreadFactory.getInstance("EventBusBoss")
    val eventFactory = EventBusBossEventFactory.INSTANCE
    disruptor = Disruptor<EventBusBossEvent>(eventFactory, DEFAULT_RING_BUFFER_SIZE, threadFactory)
    val handlers = Array(DEFAULT_WORK_HANDLER_NUMBER) { EventBusBossEventHandler.INSTANCE }
    disruptor.handleEventsWithWorkerPool(*handlers)
    disruptor.start()

    EventBusConfigLoader.configs.forEach { register(it) }

    subscribe(SubModuleEventListener.INSTANCE)
    subscribe(GoblinCallbackEventListener.INSTANCE)
    ServiceInstaller.asList(GoblinEventListener::class.java).forEach { subscribe(it) }
  }

  fun install() {
    timerEventGenerator.initialize()
  }

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
    lock.write { workers.remove(channel) }?.dispose()
  }

  fun subscribe(listener: GoblinEventListener) {
    val annotation = AnnotationUtils.getAnnotation(listener.javaClass, GoblinEventChannel::class.java)
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
    val annotation = AnnotationUtils.getAnnotation(listener.javaClass, GoblinEventChannel::class.java)
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
    val annotation = AnnotationUtils.getAnnotation(event.javaClass, GoblinEventChannel::class.java)
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

  override fun disposeBean() {
    timerEventGenerator.dispose()
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