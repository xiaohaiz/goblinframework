package org.goblinframework.remote.client.dispatcher.response

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.EventBus
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.client.invocation.RemoteClientInvocation
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.response")
class RemoteClientResponseDispatcher private constructor()
  : GoblinManagedObject(), RemoteClientResponseDispatcherMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientResponseDispatcher()
    private const val CHANNEL_NAME = "/goblin/remoter/client/response"
  }

  private val listenerReference = AtomicReference<RemoteClientResponseEventListener?>()

  override fun initializeBean() {
    val clientConfig = RemoteClientConfigManager.INSTANCE.getRemoteClientConfig()
    val bufferSize = clientConfig.getResponseBufferSize()
    val bufferWorker = clientConfig.getResponseBufferWorker()
    EventBus.register(CHANNEL_NAME, bufferSize, bufferWorker)
    val listener = RemoteClientResponseEventListener()
    EventBus.subscribe(CHANNEL_NAME, listener)
    listenerReference.set(listener)
  }

  fun onResponse(invocation: RemoteClientInvocation) {
    check(listenerReference.get() != null) { "Initialization is required" }
    val event = RemoteClientResponseEvent(invocation)
    EventBus.publish(CHANNEL_NAME, event).addDiscardListener {
      logger.warn("WARNING: Remote client response event ring buffer full")
    }
  }

  override fun disposeBean() {
    listenerReference.getAndSet(null)?.run {
      EventBus.unsubscribe(CHANNEL_NAME, this)
      this.dispose()
      EventBus.unregister(CHANNEL_NAME)
    }
  }
}