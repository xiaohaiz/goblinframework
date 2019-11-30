package org.goblinframework.remote.server.dispatcher.request

import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.request")
class RemoteServerRequestEventListener internal constructor()
  : GoblinManagedObject(), GoblinEventListener, RemoteServerRequestEventListenerMXBean {

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is RemoteServerRequestEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as RemoteServerRequestEvent
    val invocation = event.invocation
  }
}