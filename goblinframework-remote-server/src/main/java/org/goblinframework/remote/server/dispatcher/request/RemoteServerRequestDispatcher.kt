package org.goblinframework.remote.server.dispatcher.request

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.EventBus
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.core.protocol.RemoteResponseCode
import org.goblinframework.remote.server.dispatcher.response.RemoteServerResponseDispatcher
import org.goblinframework.remote.server.invocation.RemoteServerInvocation
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import org.goblinframework.transport.server.handler.TransportRequestContext
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remote.server.request")
class RemoteServerRequestDispatcher private constructor()
  : GoblinManagedObject(), RemoteServerRequestDispatcherMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerRequestDispatcher()
    private const val CHANNEL_NAME = "/goblin/remoter/server/request"
  }

  private val eventListenerReference = AtomicReference<RemoteServerRequestEventListener?>()

  override fun initializeBean() {
    val serverConfig = RemoteServerConfigManager.INSTANCE.getRemoteServerConfig()
    val bufferSize = serverConfig.getRequestBufferSize()
    val bufferWorker = serverConfig.getRequestBufferWorker()
    EventBus.register(CHANNEL_NAME, bufferSize, bufferWorker)
    val eventListener = RemoteServerRequestEventListener()
    EventBus.subscribe(CHANNEL_NAME, eventListener)
    eventListenerReference.set(eventListener)
  }

  fun onRequest(ctx: TransportRequestContext) {
    check(eventListenerReference.get() != null) { "Initialization is required" }
    val invocation = RemoteServerInvocation(ctx)
    val event = RemoteServerRequestEvent(invocation)
    EventBus.publish(CHANNEL_NAME, event).addListener {
      if (EventBus.isRingBufferFull(it)) {
        logger.error("{SERVER_BACK_PRESSURE_ERROR} " +
            "Remote server event ring buffer full, reject request from [{}]",
            invocation.context.asClientText())
        invocation.writeError(RemoteResponseCode.SERVER_BACK_PRESSURE_ERROR)
        RemoteServerResponseDispatcher.INSTANCE.onResponse(invocation)
      }
    }
  }

  override fun disposeBean() {
    eventListenerReference.getAndSet(null)?.run {
      val eventListener = this
      EventBus.unsubscribe(CHANNEL_NAME, eventListener)
      eventListener.dispose()
      EventBus.unregister(CHANNEL_NAME)
    }
  }
}