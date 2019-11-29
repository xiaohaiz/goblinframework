package org.goblinframework.remote.server.dispatcher.response

import org.goblinframework.core.event.EventBus
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("RemoteServer")
@GoblinManagedLogger("goblin.remoter.server.response")
class RemoteServerResponseDispatcher private constructor()
  : GoblinManagedObject(), RemoteServerResponseDispatcherMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerResponseDispatcher()
    private const val CHANNEL_NAME = "/goblin/remoter/server/response"
  }

  private val eventListenerReference = AtomicReference<RemoteServerResponseEventListener?>()

  override fun initializeBean() {
    val serverConfig = RemoteServerConfigManager.INSTANCE.getRemoteServerConfig()
    val bufferSize = serverConfig.getResponseBufferSize()
    val bufferWorker = serverConfig.getResponseBufferWorker()
    EventBus.register(CHANNEL_NAME, bufferSize, bufferWorker)
    val eventListener = RemoteServerResponseEventListener()
    EventBus.subscribe(CHANNEL_NAME, eventListener)
    eventListenerReference.set(eventListener)
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