package org.goblinframework.transport.client.handler

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.transport.client.setting.ClientSetting
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean("TRANSPORT.CLIENT")
class TransportClient internal constructor(val setting: ClientSetting,
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

  fun connectFuture(): TransportClientConnectFuture {
    return clientRef.get().connectFuture
  }

  fun disconnectFuture(): TransportClientDisconnectFuture {
    return clientRef.get().disconnectFuture
  }

  fun onStateChange(state: TransportClientState) {
    when (state) {
      TransportClientState.CONNECTING -> return
      TransportClientState.CONNECTED -> return
      TransportClientState.HANDSHAKED -> return
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

  internal fun close() {
    unregisterIfNecessary()
    clientRef.get().close()
  }
}