package org.goblinframework.core.event.context

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventException
import org.goblinframework.core.event.GoblinEventFuture
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class GoblinEventContextImpl
internal constructor(private val channel: String,
                     private val event: GoblinEvent,
                     private val future: GoblinEventFuture) : GoblinEventContext {

  private val state = AtomicReference(GoblinEventState.SUCCESS)
  private val taskCount = AtomicReference<AtomicInteger>()
  private val exceptions = Collections.synchronizedList(LinkedList<Throwable>())
  private val extensions = ConcurrentHashMap<String, Any>()

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

  internal fun future(): GoblinEventFuture {
    return future
  }

  internal fun initializeTaskCount(count: Int) {
    taskCount.set(AtomicInteger(count))
  }

  internal fun finishTask() {
    val c = taskCount.get()
    check(c != null)
    if (c.decrementAndGet() == 0) {
      complete()
    }
  }

  internal fun exceptionCaught(cause: Throwable) {
    exceptions.add(cause)
    state.set(GoblinEventState.FAILURE)
  }

  internal fun throwException() {
    val c = taskCount.get()
    if (c == null) {
      // task not started yet
      throw GoblinEventException(exceptions.first())
    } else {
      val total = c.get()
      throw GoblinEventException(total, exceptions)
    }
  }

  internal fun complete() {
    future.complete(this)
  }

}
