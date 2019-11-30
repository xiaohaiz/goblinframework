package org.goblinframework.remote.server.dispatcher.request

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.EventBus
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
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

  override fun disposeBean() {
    eventListenerReference.getAndSet(null)?.run {
      val eventListener = this
      EventBus.unsubscribe(CHANNEL_NAME, eventListener)
      eventListener.dispose()
      EventBus.unregister(CHANNEL_NAME)
    }
  }
}