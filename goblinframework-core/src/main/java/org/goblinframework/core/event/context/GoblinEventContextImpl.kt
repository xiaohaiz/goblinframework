package org.goblinframework.core.event.context

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventState
import org.goblinframework.core.event.exception.*
import org.goblinframework.core.util.GoblinReferenceCount
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

class GoblinEventContextImpl
internal constructor(private val channel: String,
                     private val event: GoblinEvent,
                     private val future: GoblinEventFutureImpl) : GoblinEventContext {

  private val state = AtomicReference(GoblinEventState.SUCCESS)
  private val extensions = ConcurrentHashMap<String, Any>()
  private var taskCount = 0
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

  override fun isDiscard(): Boolean {
    return state.get() === GoblinEventState.DISCARD
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
    if (ex is EventBossBufferFullException) {
      state.set(GoblinEventState.DISCARD)
    } else {
      state.set(GoblinEventState.FAILURE)
    }
    bossExceptions.add(ex)
  }

  fun workerExceptionCaught(taskId: Int, ex: EventWorkerException) {
    if (taskCount == 1 && ex is EventWorkerBufferFullException) {
      state.set(GoblinEventState.DISCARD)
    } else {
      state.set(GoblinEventState.FAILURE)
    }
    workerExceptions[taskId] = ex
    if (taskCount != 1 && workerExceptions.size == taskCount) {
      val fullExceptionCount = workerExceptions.values.filterIsInstance(EventWorkerBufferFullException::class.java).size
      if (fullExceptionCount == taskCount) {
        state.set(GoblinEventState.DISCARD)
      }
    }
  }

  fun initializeTask(count: Int) {
    taskCount = count
    taskReferenceCount = GoblinReferenceCount(count)
  }

  fun finishTask() {
    if (taskReferenceCount!!.release()) {
      complete()
    }
  }

  fun throwExceptionIfNecessary(): EventBusException? {
    if (isSuccess) {
      return null
    }
    return EventBusException(bossExceptions.firstOrNull(), workerExceptions, taskCount)
  }

  internal fun complete() {
    future.complete(this)
  }

}
