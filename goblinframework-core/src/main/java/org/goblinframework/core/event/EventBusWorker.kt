package org.goblinframework.core.event

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.core.concurrent.NamedDaemonThreadFactory
import org.goblinframework.core.management.GoblinManagedBean
import org.goblinframework.core.management.GoblinManagedObject
import org.goblinframework.core.util.ThreadUtils
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read

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
  private val listeners = mutableListOf<GoblinEventListener>()

  init {
    val threadFactory = NamedDaemonThreadFactory.getInstance("EventBusWorker-${config.channel}")
    val eventFactory = EventBusWorkerEventFactory.INSTANCE
    disruptor = Disruptor<EventBusWorkerEvent>(eventFactory, config.ringBufferSize, threadFactory)
    val n = if (config.workHandlers <= 0) ThreadUtils.estimateThreads() else config.workHandlers
    val handlers = Array(n) { EventBusWorkerEventHandler.INSTANCE }
    disruptor.handleEventsWithWorkerPool(*handlers)
    disruptor.start()
  }

  internal fun lookup(ctx: GoblinEventContext): List<GoblinEventListener> {
    return lock.read { listeners.filter { it.accept(ctx) }.toList() }
  }

  internal fun public(ctx: GoblinEventContext, listeners: List<GoblinEventListener>) {

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