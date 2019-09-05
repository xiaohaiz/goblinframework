package org.goblinframework.core.event

import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class GoblinEventContextImpl
internal constructor(private val channel: String,
                     private val event: GoblinEvent,
                     private val future: GoblinEventFuture) : GoblinEventContext {

  private val state = AtomicReference(GoblinEventState.SUCCESS)
  private val taskCount = AtomicReference<AtomicInteger>()
  private val exceptions = Collections.synchronizedList(LinkedList<Throwable>())

  override fun getChannel(): String {
    return channel
  }

  override fun getEvent(): GoblinEvent {
    return event
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

  internal fun complete() {
    future.complete(this)
  }

}
