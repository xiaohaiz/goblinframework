package org.goblinframework.transport.client.channel

import org.goblinframework.api.core.ReferenceCount
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.GoblinReferenceCount
import org.goblinframework.core.util.MapUtils
import org.goblinframework.core.util.StopWatch
import org.goblinframework.core.util.ThreadUtils
import org.goblinframework.transport.client.setting.TransportClientSetting
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean("TransportClient")
class TransportClient internal constructor(val setting: TransportClientSetting,
                                           val clientManager: TransportClientManager)
  : GoblinManagedObject(), ReferenceCount, TransportClientMXBean {

  private val clientReference: AtomicReference<TransportClientImpl>
  private val referenceCount = GoblinReferenceCount()
  private val reconnectTimes = LongAdder()

  init {
    val impl = TransportClientImpl(this)
    clientReference = AtomicReference(impl)
  }

  fun available(): Boolean {
    return clientReference.get().available()
  }

  fun stateChannel(): TransportClientChannel {
    return clientReference.get().stateChannel()
  }

  fun connectFuture(): TransportClientConnectFuture {
    return clientReference.get().connectFuture
  }

  fun disconnectFuture(): TransportClientDisconnectFuture {
    return clientReference.get().disconnectFuture
  }

  internal fun sendHeartbeat() {
    clientReference.get().sendHeartbeat()
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
      clientReference.getAndSet(TransportClientImpl(this)).close()
      reconnectTimes.increment()
    } else {
      clientManager.closeConnection(setting.name())
    }
  }

  override fun count(): Int {
    return referenceCount.count()
  }

  override fun retain() {
    referenceCount.retain()
  }

  override fun retain(increment: Int) {
    referenceCount.retain(increment)
  }

  override fun release(): Boolean {
    return referenceCount.release()
  }

  override fun release(decrement: Int): Boolean {
    return referenceCount.release(decrement)
  }

  override fun getServerId(): String? {
    return setting.serverId()
  }

  override fun getServerName(): String? {
    val extensions = clientReference.get().handshakeResponseReference.get()?.extensions
    return MapUtils.getString(extensions, "serverName")
  }

  override fun getServerHost(): String {
    return setting.serverHost()
  }

  override fun getServerPort(): Int {
    return setting.serverPort()
  }

  override fun disposeBean() {
    val watch = StopWatch()
    while (count() > 0) {
      ThreadUtils.sleepCurrentThread(100)
      if (watch.time >= 3000) {
        break
      }
    }
    clientReference.get().close()
  }
}