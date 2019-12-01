package org.goblinframework.remote.client.dispatcher.response

import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("RemoteClient")
class RemoteClientResponseEventListener internal constructor()
  : GoblinManagedObject(), GoblinEventListener, RemoteClientResponseEventListenerMXBean {

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is RemoteClientResponseEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as RemoteClientResponseEvent
    val invocation = event.invocation
  }
}