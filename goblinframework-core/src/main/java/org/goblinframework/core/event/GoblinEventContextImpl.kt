package org.goblinframework.core.event

import java.util.concurrent.atomic.AtomicReference

class GoblinEventContextImpl
internal constructor(private val channel: String,
                     private val event: GoblinEvent,
                     private val future: GoblinEventFuture) : GoblinEventContext {

  private val status = AtomicReference(GoblinEventState.SUCCESS)

  override fun getChannel(): String {
    return channel
  }

  override fun getEvent(): GoblinEvent {
    return event
  }

  internal fun future(): GoblinEventFuture {
    return future
  }

  internal fun complete(state: GoblinEventState? = null) {
    state?.run {
      this@GoblinEventContextImpl.status.set(this)
    }
    future.complete(this)
  }

}
