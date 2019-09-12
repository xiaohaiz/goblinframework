package org.goblinframework.transport.client.manager

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.transport.client.setting.ClientSetting

@GoblinManagedBean("TRANSPORT.CLIENT")
class TransportClient internal constructor(val setting: ClientSetting,
                                           val clientManager: TransportClientManager)
  : GoblinManagedObject(), TransportClientMXBean {

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

  }

  internal fun close() {
    unregisterIfNecessary()
  }
}