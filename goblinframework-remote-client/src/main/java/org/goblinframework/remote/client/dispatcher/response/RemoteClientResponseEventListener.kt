package org.goblinframework.remote.client.dispatcher.response

import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.client.module.exception.ClientDecodeResponseException

@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.response")
class RemoteClientResponseEventListener internal constructor()
  : GoblinManagedObject(), GoblinEventListener, RemoteClientResponseEventListenerMXBean {

  private val responseDecoder = RemoteClientResponseDecoder()

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is RemoteClientResponseEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as RemoteClientResponseEvent
    val invocation = event.invocation
    invocation.transportError?.run {
      invocation.complete(null, this)
      return
    }
    val content = invocation.flight.responseReader().response().payload
    val decoded = responseDecoder.decode(content)
    decoded.error?.run {
      logger.error("{CLIENT_DECODE_RESPONSE_ERROR} " +
          "Exception raised when decoding server response message", this)
      invocation.complete(null, ClientDecodeResponseException(this))
      return
    }
  }

  override fun disposeBean() {
    responseDecoder.dispose()
  }
}