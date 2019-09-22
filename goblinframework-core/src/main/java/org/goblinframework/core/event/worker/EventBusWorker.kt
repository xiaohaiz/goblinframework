package org.goblinframework.core.event.worker

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.concurrent.NamedDaemonThreadFactory
import org.goblinframework.core.event.EventBus
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventException
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.event.config.EventBusConfig
import org.goblinframework.core.event.context.GoblinEventContextImpl
import org.goblinframework.core.event.exception.WorkerRingBufferFullException
import org.goblinframework.core.util.SystemUtils
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean(type = "core")
class EventBusWorker
internal constructor(private val config: EventBusConfig)
  : GoblinManagedObject(), EventBusWorkerMXBean {

  companion object {
    private const val DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS = 15
  }

  private val disruptor: Disruptor<EventBusWorkerEvent>
  private val lock = ReentrantReadWriteLock()
  private val listeners = IdentityHashMap<GoblinEventListener, Instant>()

  init {
    val threadFactory = NamedDaemonThreadFactory.getInstance("EventBusWorker-${config.channel}")
    val eventFactory = EventBusWorkerEventFactory.INSTANCE
    disruptor = Disruptor<EventBusWorkerEvent>(eventFactory, config.ringBufferSize, threadFactory)
    val n = if (config.workHandlers <= 0) SystemUtils.estimateThreads() else config.workHandlers
    val handlers = Array(n) { EventBusWorkerEventHandler.INSTANCE }
    disruptor.handleEventsWithWorkerPool(*handlers)
    disruptor.start()
  }

  internal fun subscribe(listener: GoblinEventListener) {
    lock.write {
      if (listeners[listener] != null) {
        throw GoblinEventException("Listener [$listener] already subscribed on channel [${config.channel}]")
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
      ctx.exceptionCaught(WorkerRingBufferFullException(ctx.channel))
      ctx.finishTask()
    }
  }

  override fun disposeBean() {
    try {
      disruptor.shutdown(DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
      EventBus.LOGGER.info("EventBusWorker [${config.channel}] closed")
    } catch (ex: TimeoutException) {
      EventBus.LOGGER.warn("EventBusWorker [${config.channel}] close timeout", ex)
    }
  }
}