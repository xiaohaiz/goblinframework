package org.goblinframework.remote.client.dispatcher.request

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.event.EventBus
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedLogger
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.remote.client.invocation.RemoteClientInvocation
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager
import org.goblinframework.remote.client.module.exception.ClientBackPressureException
import java.util.concurrent.atomic.AtomicReference

@Singleton
@GoblinManagedBean("RemoteClient")
@GoblinManagedLogger("goblin.remote.client.request")
class RemoteClientRequestDispatcher private constructor()
  : GoblinManagedObject(), RemoteClientRequestDispatcherMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientRequestDispatcher()
    private const val CHANNEL_NAME = "/goblin/remoter/client/request"
  }

  private val listenerReference = AtomicReference<RemoteClientRequestEventListener?>()

  override fun initializeBean() {
    val clientConfig = RemoteClientConfigManager.INSTANCE.getRemoteClientConfig()
    val bufferSize = clientConfig.getRequestBufferSize()
    val bufferWorker = clientConfig.getRequestBufferWorker()
    EventBus.register(CHANNEL_NAME, bufferSize, bufferWorker)
    val listener = RemoteClientRequestEventListener()
    EventBus.subscribe(CHANNEL_NAME, listener)
    listenerReference.set(listener)
  }

  fun onRequest(invocation: RemoteClientInvocation) {
    check(listenerReference.get() != null) { "Initialization is required" }
    val event = RemoteClientRequestEvent(invocation)
    EventBus.publish(CHANNEL_NAME, event).addDiscardListener {
      logger.error("{CLIENT_BACK_PRESSURE_ERROR} " +
          "Remote client event ring buffer full, reject request [service={}]",
          invocation.serviceId.asText())
      invocation.complete(null, ClientBackPressureException())
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