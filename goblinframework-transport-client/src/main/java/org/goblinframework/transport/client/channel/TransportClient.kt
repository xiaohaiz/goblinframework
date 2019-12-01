package org.goblinframework.transport.client.channel

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.transport.client.setting.TransportClientSetting
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean(type = "transport.client")
class TransportClient internal constructor(val setting: TransportClientSetting,
                                           val clientManager: TransportClientManager)
  : GoblinManagedObject(), TransportClientMXBean {

  private val clientRef = AtomicReference<TransportClientImpl>()
  private val reconnectTimes = LongAdder()

  init {
    clientRef.set(TransportClientImpl(this))
  }

  fun available(): Boolean {
    return clientRef.get().available()
  }

  fun stateChannel(): TransportClientChannel {
    return clientRef.get().stateChannel()
  }

  fun connectFuture(): TransportClientConnectFuture {
    return clientRef.get().connectFuture
  }

  fun disconnectFuture(): TransportClientDisconnectFuture {
    return clientRef.get().disconnectFuture
  }

  internal fun sendHeartbeat() {
    clientRef.get().sendHeartbeat()
  }

  fun onStateChange(state: TransportClientState) {
    when (state) {
      TransportClientState.CONNECTING -> return
      TransportClientState.CONNECTED -> return
      TransportClientState.HANDSHAKED -> {
        val handlerSetting = setting.handlerSetting()
        val connectedHandler = handlerSetting.transportClientConnectedHandler()
        connectedHandler?.handleTransportClientConnected(this)
      }
      TransportClientState.SHUTDOWN -> return
      TransportClientState.CONNECT_FAILED -> terminate()
      TransportClientState.HANDSHAKE_FAILED -> terminate()
      TransportClientState.HEARTBEAT_LOST -> terminate(true)
      TransportClientState.DISCONNECTED -> terminate(true)
    }
  }

  private fun terminate(reconnect: Boolean = false) {
    if (reconnect && setting.autoReconnect()) {
      clientRef.getAndSet(TransportClientImpl(this)).close()
      reconnectTimes.increment()
    } else {
      clientManager.closeConnection(setting.name())
    }
  }

  override fun disposeBean() {
    clientRef.get().close()
  }
}