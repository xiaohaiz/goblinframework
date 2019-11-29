package org.goblinframework.remote.server.dispatcher.response

import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("RemoteServer")
class RemoteServerResponseEventListener internal constructor()
  : GoblinManagedObject(), GoblinEventListener, RemoteServerResponseEventListenerMXBean {

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is RemoteServerResponseEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as RemoteServerResponseEvent
    val invocation = event.invocation
  }
}