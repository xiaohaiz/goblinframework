package org.goblinframework.transport.client.handler

import org.goblinframework.transport.client.manager.TransportClientImpl
import org.goblinframework.transport.client.manager.TransportClientState

class TransportClientChannel(val state: TransportClientState,
                             val client: TransportClientImpl?) {

  companion object {
    @JvmField val CONNECTING = TransportClientChannel(TransportClientState.CONNECTING, null)
    @JvmField val CONNECT_FAILED = TransportClientChannel(TransportClientState.CONNECT_FAILED, null)
    @JvmField val HANDSHAKE_FAILED = TransportClientChannel(TransportClientState.HANDSHAKE_FAILED, null)
    @JvmField val DISCONNECTED = TransportClientChannel(TransportClientState.DISCONNECTED, null)
    @JvmField val SHUTDOWN = TransportClientChannel(TransportClientState.SHUTDOWN, null)
  }
}