package org.goblinframework.remote.server.dispatcher.request

import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.core.protocol.RemoteResponseCode
import org.goblinframework.remote.server.dispatcher.response.RemoteServerResponseDispatcher

@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.request")
class RemoteServerRequestEventListener internal constructor()
  : GoblinManagedObject(), GoblinEventListener, RemoteServerRequestEventListenerMXBean {

  private val requestDecoder = RemoteServerRequestDecoder()
  private val threadPool = RemoteServerRequestThreadPool()

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is RemoteServerRequestEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as RemoteServerRequestEvent
    val invocation = event.invocation
    val payload = invocation.context.requestReader.request().payload
    val decoded = requestDecoder.decode(payload)
    decoded.error?.run {
      logger.error("{SERVER_UNMARSHAL_REQUEST_ERROR} " +
          "Exception raised when decoding request [client={}]",
          invocation.context.asClientText(), this)
      invocation.writeError(RemoteResponseCode.SERVER_UNMARSHAL_REQUEST_ERROR, this)
      RemoteServerResponseDispatcher.INSTANCE.onResponse(invocation)
      return
    }
    invocation.writeRequest(decoded.request, decoded.serializer)
    threadPool.onInvocation(invocation)
  }

  override fun disposeBean() {
    requestDecoder.dispose()
    threadPool.dispose()
  }
}