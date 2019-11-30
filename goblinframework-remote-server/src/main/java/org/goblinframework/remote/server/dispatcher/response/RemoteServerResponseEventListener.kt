package org.goblinframework.remote.server.dispatcher.response

import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.core.protocol.RemoteResponseCode

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
    var encoded = responseEncoder.encode(invocation)
    if (encoded.error != null) {
      logger.error("{SERVER_MARSHAL_RESPONSE_ERROR} " +
          "Exception raised when encoding DittoResponse [{}]",
          invocation.asText(), encoded.error)
      invocation.response.resetResult()
      invocation.response.resetError()
      invocation.response.writeCode(RemoteResponseCode.SERVER_MARSHAL_RESPONSE_ERROR)
      invocation.response.writeError(encoded.error)
      // Encode again, exception shouldn't raise this time
      encoded = responseEncoder.encode(invocation)
    }

    invocation.context.responseWriter.response().compressor = 0
    invocation.context.responseWriter.response().serializer = 0
    invocation.context.responseWriter.response().payload = encoded.content
    invocation.context.responseWriter.response().hasPayload = true
    invocation.context.responseWriter.response().rawPayload = true
    invocation.context.sendResponse()
  }

  override fun disposeBean() {
    responseEncoder.dispose()
  }
}