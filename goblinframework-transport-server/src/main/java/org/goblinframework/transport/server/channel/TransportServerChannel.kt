package org.goblinframework.transport.server.channel

import io.netty.channel.Channel
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.util.MapUtils
import org.goblinframework.transport.core.codec.TransportMessage
import org.goblinframework.transport.core.protocol.*
import org.goblinframework.transport.core.protocol.reader.TransportRequestReader
import org.goblinframework.transport.core.protocol.writer.TransportResponseWriter
import org.goblinframework.transport.server.handler.TransportRequestContext
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

@GoblinManagedBean(type = "transport.server")
class TransportServerChannel
internal constructor(private val server: TransportServerImpl,
                     private val channel: Channel)
  : GoblinManagedObject(), TransportServerChannelMXBean {

  companion object {
    private val logger = LoggerFactory.getLogger(TransportServerChannel::class.java)
  }

  private val setting = server.setting
  private val handshake = AtomicReference<HandshakeRequest>()

  fun onTransportMessage(msg: TransportMessage) {
    if (msg.message == null) {
      // unrecognized message received, ignore and return directly
      return
    }
    when (val message = msg.message) {
      is HandshakeRequest -> {
        val handler = setting.handlerSetting().handshakeRequestHandler()
        val success = handler.handleHandshakeRequest(message)
        if (success) {
          handshake.set(message)
        }
        val response = HandshakeResponse()
        response.success = success
        writeTransportMessage(TransportMessage(response, msg.serializer))
        return
      }
      is HeartbeatRequest -> {
        val response = HeartbeatResponse()
        response.token = message.token
        writeTransportMessage(TransportMessage(response, msg.serializer))
        return
      }
      is TransportRequest -> {
        val ctx = TransportRequestContext()
        ctx.channel = this
        ctx.serializer = msg.serializer
        ctx.requestReader = TransportRequestReader(message)
        ctx.responseWriter = TransportResponseWriter(ctx.requestReader)
        ctx.extensions = ConcurrentHashMap()
        val handler = setting.handlerSetting().transportRequestHandler()
        handler.handleTransportRequest(ctx)
        return
      }
      else -> logger.error("Unrecognized message received: ${msg.message}")
    }
  }

  fun writeTransportMessage(msg: TransportMessage) {
    channel.writeAndFlush(msg)
  }

  internal fun terminate() {
    if (!getClientReceiveShutdown()) {
      return
    }
    val request = ShutdownRequest()
    request.clientId = getClientId()
    val serializer = TransportProtocol.getSerializerId(ShutdownRequest::class.java)
    writeTransportMessage(TransportMessage(request, serializer))
  }

  override fun disposeBean() {
    handshake.set(null)
  }

  override fun getClientId(): String? {
    return handshake.get()?.clientId
  }

  override fun getClientName(): String? {
    return handshake.get()?.extensions?.get("clientName") as? String
  }

  override fun getClientHost(): String {
    return (channel.remoteAddress() as InetSocketAddress).address.hostAddress
  }

  override fun getClientPort(): Int {
    return (channel.remoteAddress() as InetSocketAddress).port
  }

  override fun getClientReceiveShutdown(): Boolean {
    val request = handshake.get() ?: return false
    return MapUtils.getBoolean(request.extensions, "receiveShutdown", false)
  }
}