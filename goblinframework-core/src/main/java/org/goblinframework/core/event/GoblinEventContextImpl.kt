package org.goblinframework.core.event

import org.goblinframework.core.event.context.GoblinEventFutureImpl
import org.goblinframework.core.event.exception.EventBossException
import org.goblinframework.core.event.exception.EventBusException
import org.goblinframework.core.event.exception.EventWorkerException
import org.goblinframework.core.util.GoblinReferenceCount
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class GoblinEventContextImpl
internal constructor(private val channel: String,
                     private val event: GoblinEvent,
                     private val future: GoblinEventFutureImpl) : GoblinEventContext {

  private val state = AtomicReference(GoblinEventState.SUCCESS)

  private val taskCount = AtomicReference<AtomicInteger?>()
  private val exceptions = Collections.synchronizedList(LinkedList<Throwable>())
  private val extensions = ConcurrentHashMap<String, Any>()

  private var totalTaskCount = 0
  private var taskReferenceCount: GoblinReferenceCount? = null
  private val bossExceptions = LinkedList<EventBossException>()
  private val workerExceptions = ConcurrentHashMap<Int, EventWorkerException>()

  override fun getChannel(): String {
    return channel
  }

  override fun getEvent(): GoblinEvent {
    return event
  }

  override fun isSuccess(): Boolean {
    return state.get() === GoblinEventState.SUCCESS
  }

  override fun getExtensions(): MutableMap<String, Any> {
    return extensions
  }

  override fun getExtension(name: String): Any? {
    return extensions[name]
  }

  override fun removeExtension(name: String): Any? {
    return extensions.remove(name)
  }

  override fun setExtension(name: String, extension: Any): Any? {
    return extensions.put(name, extension)
  }

  fun future(): GoblinEventFutureImpl {
    return future
  }

  fun bossExceptionCaught(ex: EventBossException) {
    state.set(GoblinEventState.FAILURE)
    if (event.isRaiseException) {
      bossExceptions.add(ex)
    }
  }

  fun workerExceptionCaught(taskId: Int, ex: EventWorkerException) {
    state.set(GoblinEventState.FAILURE)
    if (event.isRaiseException) {
      workerExceptions[taskId] = ex
    }
  }

  fun initializeTask(count: Int) {
    totalTaskCount = count
    taskReferenceCount = GoblinReferenceCount(count)
  }

  fun finishTask() {
    if (taskReferenceCount!!.release()) {
      complete()
    }
  }

  fun throwExceptionIfNecessary(): EventBusException? {
    if (isSuccess || !event.isRaiseException) {
      return null
    }
    return EventBusException(bossExceptions.firstOrNull(), workerExceptions, totalTaskCount)
  }

  override fun throwException() {
    if (isSuccess) {
      return
    }
    val c = taskCount.get()
    if (c == null) {
      // task not started yet
      throw GoblinEventException(exceptions.first())
    } else {
      throw GoblinEventException(totalTaskCount, exceptions)
    }
  }

  internal fun complete() {
    future.complete(this)
  }

}
