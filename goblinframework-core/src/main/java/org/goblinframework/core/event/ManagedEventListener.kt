package org.goblinframework.core.event

import org.goblinframework.api.common.Ordered
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "Core", name = "GoblinEventListener")
class ManagedEventListener
internal constructor(private val listener: GoblinEventListener)
  : GoblinManagedObject(), GoblinEventListener, Ordered, EventListenerMXBean {

  override fun getOrder(): Int {
    return (listener as? Ordered)?.order ?: 0
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return listener.accept(context)
  }

  override fun onEvent(context: GoblinEventContext) {
    listener.onEvent(context)
  }
}