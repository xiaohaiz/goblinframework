package org.goblinframework.core.event

class GoblinEventContextImpl
internal constructor(private val channel: String,
                     private val event: GoblinEvent,
                     private val future: GoblinEventFuture) : GoblinEventContext {

  override fun getChannel(): String {
    return channel
  }

  override fun getEvent(): GoblinEvent {
    return event
  }
}
