package org.goblinframework.core.event

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.core.concurrent.NamedDaemonThreadFactory
import org.goblinframework.core.event.config.EventBusConfig
import org.goblinframework.core.event.exception.WorkerRingBufferFullException
import org.goblinframework.core.management.GoblinManagedBean
import org.goblinframework.core.management.GoblinManagedObject
import org.goblinframework.core.util.ThreadUtils
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean("CORE")
class EventBusWorker
internal constructor(private val config: EventBusConfig)
  : GoblinManagedObject(), EventBusWorkerMXBean {

  companion object {
    private const val DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS = 15
    private val logger = LoggerFactory.getLogger(EventBusWorker::class.java)
  }

  private val disruptor: Disruptor<EventBusWorkerEvent>
  private val lock = ReentrantReadWriteLock()
  private val listeners = IdentityHashMap<GoblinEventListener, Instant>()

  init {
    val threadFactory = NamedDaemonThreadFactory.getInstance("EventBusWorker-${config.channel}")
    val eventFactory = EventBusWorkerEventFactory.INSTANCE
    disruptor = Disruptor<EventBusWorkerEvent>(eventFactory, config.ringBufferSize, threadFactory)
    val n = if (config.workHandlers <= 0) ThreadUtils.estimateThreads() else config.workHandlers
    val handlers = Array(n) { EventBusWorkerEventHandler.INSTANCE }
    disruptor.handleEventsWithWorkerPool(*handlers)
    disruptor.start()
  }

  internal fun subscribe(listener: GoblinEventListener) {
    lock.write {
      if (listeners[listener] != null) return
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

  internal fun close() {
    unregisterIfNecessary()
    try {
      disruptor.shutdown(DEFAULT_SHUTDOWN_TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
      logger.info("EventBusWorker [${config.channel}] closed")
    } catch (ex: TimeoutException) {
      logger.warn("EventBusWorker [${config.channel}] close timeout", ex)
    }
  }
}