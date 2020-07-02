package org.goblinframework.transport.client.channel

import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.apache.commons.collections4.map.LRUMap
import org.goblinframework.core.system.GoblinSystem
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.core.util.StringUtils
import org.goblinframework.transport.client.handler.TransportResponseContext
import org.goblinframework.transport.client.module.TransportClientModule
import org.goblinframework.transport.codec.TransportMessage
import org.goblinframework.transport.codec.TransportMessageDecoder
import org.goblinframework.transport.codec.TransportMessageEncoder
import org.goblinframework.transport.protocol.*
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class TransportClientImpl internal constructor(private val client: TransportClient) {

  companion object {
    private val logger = LoggerFactory.getLogger(TransportClientImpl::class.java)
  }

  internal val handshakeResponseReference = AtomicReference<HandshakeResponse?>()
  private val heartbeatInProgress = Collections.synchronizedMap(LRUMap<String, HeartbeatRequest>(32))
  private val stateChannelReference = AtomicReference<TransportClientChannel>(TransportClientChannel.CONNECTING)
  private val channelReference = AtomicReference<Channel?>()
  private val worker: NioEventLoopGroup

  internal val connectFuture = TransportClientConnectFuture()
  internal val disconnectFuture = TransportClientDisconnectFuture(client.clientManager)


  init {
    val setting = client.setting
    val threads = client.setting.workerThreads()
    worker = NioEventLoopGroup(threads)
    val bootstrap = Bootstrap()
    bootstrap.group(worker)
    bootstrap.channel(NioSocketChannel::class.java)
    bootstrap.option(ChannelOption.SO_KEEPALIVE, setting.keepAlive())
    bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, setting.connectTimeoutInMillis())
    val initializer = object : ChannelInitializer<SocketChannel>() {
      override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()
        if (setting.debugMode()) {
          pipeline.addLast(LoggingHandler(LogLevel.DEBUG))
        }
        pipeline.addLast("encoder", TransportMessageEncoder.getInstance())
        pipeline.addLast("decoder", TransportMessageDecoder.newInstance())
        pipeline.addLast(TransportClientChannelHandler(this@TransportClientImpl))
      }
    }
    bootstrap.handler(initializer)
    val remoteAddress = InetSocketAddress(setting.serverHost(), setting.serverPort())
    bootstrap.connect(remoteAddress).addListener(object : ChannelFutureListener {
      override fun operationComplete(future: ChannelFuture) {
        if (!future.isSuccess) {
          updateStateChannel(TransportClientChannel.CONNECT_FAILED)
          val cause = TransportClientException("Exception opening connection", future.cause())
          connectFuture.complete(client, cause)
          return
        }
        val channel = future.channel()
        channelReference.set(channel)
        updateStateChannel(TransportClientChannel(TransportClientState.CONNECTED, this@TransportClientImpl))

        val request = HandshakeRequest()
        request.serverId = setting.serverId()
        request.clientId = GoblinSystem.applicationId()
        request.extensions = linkedMapOf()
        request.extensions["clientName"] = GoblinSystem.applicationName()
        request.extensions["clientLanguage"] = "java/kotlin"
        request.extensions["receiveShutdown"] = setting.receiveShutdown()
        val serializer = TransportProtocol.getSerializerId(request.javaClass)
        writeTransportMessage(TransportMessage(request, serializer))
      }
    })
  }

  @Synchronized
  fun updateStateChannel(sc: TransportClientChannel) {
    val previous = stateChannelReference.get().state
    if (previous === TransportClientState.SHUTDOWN) {
      return
    }
    stateChannelReference.set(sc)
    if (logger.isDebugEnabled) {
      logger.debug("{} state changed: {} -> {}", this, previous, sc.state)
    }
    client.onStateChange(sc.state)
  }

  fun available(): Boolean {
    return stateChannelReference.get().available()
  }

  fun stateChannel(): TransportClientChannel {
    return stateChannelReference.get()
  }

  fun sendHeartbeat() {
    if (!client.setting.sendHeartbeat()) {
      return
    }
    if (!available()) {
      return
    }
    val request = HeartbeatRequest()
    request.token = RandomUtils.nextObjectId()
    val serializer = TransportProtocol.getSerializerId(request::class.java)
    writeTransportMessage(TransportMessage(request, serializer))
    heartbeatInProgress[request.token] = request
    if (heartbeatInProgress.size >= TransportClientModule.maxSufferanceHeartLost) {
      updateStateChannel(TransportClientChannel.HEARTBEAT_LOST)
    }
  }

  fun onTransportMessage(msg: TransportMessage) {
    if (msg.message == null) {
      // unrecognized message received, ignore and return directly
      return
    }
    when (val message = msg.message) {
      is HandshakeResponse -> {
        if (message.success) {
          handshakeResponseReference.set(message)
          updateStateChannel(TransportClientChannel(TransportClientState.HANDSHAKED, this))
        } else {
          updateStateChannel(TransportClientChannel.HANDSHAKE_FAILED)
        }
        connectFuture.complete(client)
        return
      }
      is HeartbeatResponse -> {
        message.token?.run {
          heartbeatInProgress.remove(this)
        }
        return
      }
      is ShutdownRequest -> {
        val handler = client.setting.shutdownRequestHandler()
        val success = handler.handleShutdownRequest(message)
        if (success) {
          val name = client.setting.name()
          client.clientManager.closeConnection(name)
        }
        return
      }
      is TransportResponse -> {
        val handler = client.setting.handlerSetting().transportResponseHandler()
        val ctx = TransportResponseContext()
        ctx.response = message
        if (ctx.response.extensions == null) {
          ctx.response.extensions = linkedMapOf()
        }
        ctx.response.extensions["SERVER_ID"] = StringUtils.defaultString(client.setting.serverId())
        ctx.response.extensions["SERVER_HOST"] = client.setting.serverHost()
        ctx.response.extensions["SERVER_PORT"] = client.setting.serverPort().toString()
        handler.handleTransportResponse(ctx)
        return
      }
      else -> logger.error("Unrecognized message received: $message")
    }
  }

  fun writeTransportMessage(msg: TransportMessage) {
    channelReference.get()?.writeAndFlush(msg)
  }

  internal fun close() {
    updateStateChannel(TransportClientChannel.SHUTDOWN)
    heartbeatInProgress.clear()
    worker.shutdownGracefully().addListener {
      if (!it.isSuccess) {
        val cause = TransportClientException("Exception closing connection", it.cause())
        disconnectFuture.complete(client, cause)
      } else {
        disconnectFuture.complete(client)
      }
    }
  }

}