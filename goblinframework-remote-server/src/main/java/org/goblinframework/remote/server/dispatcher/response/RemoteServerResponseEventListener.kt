package org.goblinframework.remote.server.dispatcher.response

import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.response")
class RemoteServerResponseEventListener internal constructor()
  : GoblinManagedObject(), GoblinEventListener, RemoteServerResponseEventListenerMXBean {

  private val responseEncoder = RemoteServerResponseEncoder()

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is RemoteServerResponseEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as RemoteServerResponseEvent
    val invocation = event.invocation
    if (!invocation.context.requestReader.request().response) {
      return
    }

  }

  override fun disposeBean() {
    responseEncoder.dispose()
  }
}