package org.goblinframework.transport.client.handler

import org.goblinframework.transport.core.protocol.TransportMessage
import org.goblinframework.transport.core.protocol.TransportProtocol

class TransportClientChannel(val state: TransportClientState,
                             val client: TransportClientImpl?) {

  companion object {
    @JvmField val CONNECTING = TransportClientChannel(TransportClientState.CONNECTING, null)
    @JvmField val CONNECT_FAILED = TransportClientChannel(TransportClientState.CONNECT_FAILED, null)
    @JvmField val HANDSHAKE_FAILED = TransportClientChannel(TransportClientState.HANDSHAKE_FAILED, null)
    @JvmField val DISCONNECTED = TransportClientChannel(TransportClientState.DISCONNECTED, null)
    @JvmField val HEARTBEAT_LOST = TransportClientChannel(TransportClientState.HEARTBEAT_LOST, null)
    @JvmField val SHUTDOWN = TransportClientChannel(TransportClientState.SHUTDOWN, null)
  }

  fun available(): Boolean {
    return state === TransportClientState.HANDSHAKED
  }

  fun writeMessage(msg: Any) {
    if (msg is TransportMessage) {
      client?.writeTransportMessage(msg)
    } else {
      val serializer = TransportProtocol.getSerializerId(msg.javaClass)
      client?.writeTransportMessage(TransportMessage(msg, serializer))
    }
  }
}