package org.goblinframework.transport.client.manager

import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import org.goblinframework.core.bootstrap.GoblinSystem
import org.goblinframework.transport.client.handler.TransportClientChannel
import org.goblinframework.transport.client.handler.TransportClientChannelHandler
import org.goblinframework.transport.core.codec.TransportMessageDecoder
import org.goblinframework.transport.core.codec.TransportMessageEncoder
import org.goblinframework.transport.core.protocol.HandshakeRequest
import org.goblinframework.transport.core.protocol.HandshakeResponse
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class TransportClientImpl
internal constructor(private val client: TransportClient) {

  companion object {
    private val logger = LoggerFactory.getLogger(TransportClientImpl::class.java)
  }

  private val stateChannelLock = ReentrantLock()
  private val stateChannelRef = AtomicReference<TransportClientChannel>(TransportClientChannel.CONNECTING)
  private val channelRef = AtomicReference<Channel>()
  private val worker: NioEventLoopGroup

  internal val connectFuture = TransportClientConnectFuture()
  internal val disconnectFuture = TransportClientDisconnectFuture(client.clientManager)

  init {
    val setting = client.setting
    val threads = client.setting.workerThreads
    worker = NioEventLoopGroup(threads)
    val bootstrap = Bootstrap()
    bootstrap.group(worker)
    bootstrap.channel(NioSocketChannel::class.java)
    bootstrap.option(ChannelOption.SO_KEEPALIVE, setting.isKeepAlive)
    bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, setting.connectTimeoutInMillis)
    val initializer = object : ChannelInitializer<SocketChannel>() {
      override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()
        pipeline.addLast("encoder", TransportMessageEncoder.getInstance())
        pipeline.addLast("decoder", TransportMessageDecoder.newInstance())
        pipeline.addLast(TransportClientChannelHandler(this@TransportClientImpl))
      }
    }
    bootstrap.handler(initializer)
    val remoteAddress = InetSocketAddress(setting.serverHost, setting.serverPort)
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
        request.serverId = setting.serverId
        request.clientId = GoblinSystem.applicationId()
        writeMessage(request)
      }
    })
  }

  fun updateStateChannel(sc: TransportClientChannel) {
    stateChannelLock.withLock {
      val previous = stateChannelRef.get().state
      if (previous === TransportClientState.SHUTDOWN) {
        return
      }
      stateChannelRef.set(sc)
      if (logger.isDebugEnabled) {
        logger.debug("{}} state changed: {} -> {}", this, previous, sc.state)
      }
      client.onStateChange(sc.state)
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
      else -> logger.error("Unrecognized message received: $msg")
    }
  }

  internal fun close() {
    updateStateChannel(TransportClientChannel.SHUTDOWN)
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