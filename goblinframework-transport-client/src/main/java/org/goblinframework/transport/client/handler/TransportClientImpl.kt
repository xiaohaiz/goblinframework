package org.goblinframework.transport.client.handler

import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.apache.commons.collections4.map.LRUMap
import org.goblinframework.core.bootstrap.GoblinSystem
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.transport.client.module.TransportClientModule
import org.goblinframework.transport.core.codec.TransportMessageDecoder
import org.goblinframework.transport.core.codec.TransportMessageEncoder
import org.goblinframework.transport.core.protocol.*
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class TransportClientImpl
internal constructor(private val client: TransportClient) {

  companion object {
    private val logger = LoggerFactory.getLogger(TransportClientImpl::class.java)
  }

  private val heartbeatInProgress = Collections.synchronizedMap(LRUMap<String, HeartbeatRequest>(32))
  private val stateChannelRef = AtomicReference<TransportClientChannel>(TransportClientChannel.CONNECTING)
  private val channelRef = AtomicReference<Channel>()
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
        pipeline.addLast(LoggingHandler(LogLevel.DEBUG))
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
        channelRef.set(channel)
        updateStateChannel(TransportClientChannel(TransportClientState.CONNECTED, this@TransportClientImpl))

        val request = HandshakeRequest()
        request.serverId = setting.serverId()
        request.clientId = GoblinSystem.applicationId()
        request.extensions = linkedMapOf()
        request.extensions["clientLanguage"] = "java/kotlin"
        request.extensions["receiveShutdown"] = setting.receiveShutdown()
        writeMessage(request)
      }
    })
  }

  @Synchronized
  fun updateStateChannel(sc: TransportClientChannel) {
    val previous = stateChannelRef.get().state
    if (previous === TransportClientState.SHUTDOWN) {
      return
    }
    stateChannelRef.set(sc)
    if (logger.isDebugEnabled) {
      logger.debug("{} state changed: {} -> {}", this, previous, sc.state)
    }
    client.onStateChange(sc.state)
  }

  fun available(): Boolean {
    return stateChannelRef.get().available()
  }

  fun stateChannel(): TransportClientChannel {
    return stateChannelRef.get()
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
    writeMessage(request)
    heartbeatInProgress[request.token] = request
    if (heartbeatInProgress.size >= TransportClientModule.maxSufferanceHeartLost) {
      updateStateChannel(TransportClientChannel.HEARTBEAT_LOST)
    }
  }

  fun writeMessage(msg: Any) {
    channelRef.get()?.writeAndFlush(msg)
  }

  fun onMessage(msg: Any) {
    when (msg) {
      is HandshakeResponse -> {
        if (msg.success) {
          updateStateChannel(TransportClientChannel(TransportClientState.HANDSHAKED, this))
        } else {
          updateStateChannel(TransportClientChannel.HANDSHAKE_FAILED)
        }
        connectFuture.complete(client)
        return
      }
      is HeartbeatResponse -> {
        msg.token?.run {
          heartbeatInProgress.remove(this)
        }
        return
      }
      is ShutdownRequest -> {
        val handler = client.setting.shutdownRequestHandler()
        val success = handler.handleShutdownRequest(msg)
        if (success) {
          val name = client.setting.name()
          client.clientManager.closeConnection(name)
        }
        return
      }
      else -> logger.error("Unrecognized message received: $msg")
    }
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