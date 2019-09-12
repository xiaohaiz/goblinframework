package org.goblinframework.transport.server.channel

import io.netty.channel.Channel
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.transport.core.protocol.HandshakeRequest
import org.goblinframework.transport.core.protocol.HandshakeResponse
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean("TRANSPORT.SERVER")
class TransportServerChannel
internal constructor(private val server: TransportServerImpl,
                     private val channel: Channel)
  : GoblinManagedObject(), TransportServerChannelMXBean {

  companion object {
    private val logger = LoggerFactory.getLogger(TransportServerChannel::class.java)
  }

  private val handshake = AtomicReference<HandshakeRequest>()

  fun onMessage(msg: Any) {
    when (msg) {
      is HandshakeRequest -> {
        val handler = server.setting.handshakeRequestHandler()
        val success = handler.handleHandshakeRequest(msg)
        val response = HandshakeResponse()
        response.success = success
        writeMessage(response)
        return
      }
      else -> logger.error("Unrecognized message received: $msg")
    }

  }

  fun writeMessage(msg: Any) {
    channel.writeAndFlush(msg)
  }

  internal fun terminate() {
  }

  internal fun close() {
    unregisterIfNecessary()
    handshake.set(null)
  }
}