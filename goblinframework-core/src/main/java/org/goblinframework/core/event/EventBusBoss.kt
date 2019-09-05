package org.goblinframework.core.event

import com.lmax.disruptor.TimeoutException
import com.lmax.disruptor.dsl.Disruptor
import org.goblinframework.core.concurrent.NamedDaemonThreadFactory
import org.goblinframework.core.event.exception.BossRingBufferFullException
import org.goblinframework.core.management.GoblinManagedBean
import org.goblinframework.core.management.GoblinManagedObject
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@GoblinManagedBean("CORE")
class EventBusBoss private constructor() : GoblinManagedObject(), EventBusBossMXBean {

  companion object {
    private val logger = LoggerFactory.getLogger(EventBusBoss::class.java)

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
      logger.info("EventBusBoss closed")
    } catch (ex: TimeoutException) {
      logger.warn("EventBusBoss close timeout", ex)
    }
    lock.write {
      workers.values.reversed().forEach { it.close() }
      workers.clear()
    }
    logger.info("EventBus closed")
  }
}