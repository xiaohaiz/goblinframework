package org.goblinframework.transport.server.channel

import io.netty.channel.Channel
import org.apache.commons.collections4.MapUtils
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.transport.core.protocol.*
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
        if (success) {
          handshake.set(msg)
        }
        val response = HandshakeResponse()
        response.success = success
        writeMessage(response)
        return
      }
      is HeartbeatRequest -> {
        val response = HeartbeatResponse()
        response.token = msg.token
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
    if (!getClientReceiveShutdown()) {
      return
    }
    val request = ShutdownRequest()
    request.clientId = getClientId()
    writeMessage(request)
  }

  internal fun close() {
    unregisterIfNecessary()
    handshake.set(null)
  }

  override fun getClientId(): String? {
    return handshake.get()?.clientId
  }

  override fun getClientReceiveShutdown(): Boolean {
    val request = handshake.get() ?: return false
    return MapUtils.getBoolean(request.extensions, "receiveShutdown", false)
  }
}