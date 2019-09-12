package org.goblinframework.transport.server.channel

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.goblinframework.transport.core.codec.TransportMessageDecoder
import org.goblinframework.transport.core.codec.TransportMessageEncoder
import org.goblinframework.transport.server.setting.ServerSetting
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

class TransportServerImpl(val setting: ServerSetting) {

  companion object {
    private val logger = LoggerFactory.getLogger(TransportServerImpl::class.java)
  }

  private val boss: NioEventLoopGroup
  private val worker: NioEventLoopGroup
  private val channelManager: TransportServerChannelManager

  internal val host: String
  internal val port: Int

  init {
    val socketAddress = InetSocketAddress(setting.host(), setting.port())

    boss = NioEventLoopGroup(setting.bossThreads())
    worker = NioEventLoopGroup(setting.bossThreads())

    channelManager = TransportServerChannelManager(this)

    val bootstrap = ServerBootstrap()
        .group(boss, worker)
        .channel(NioServerSocketChannel::class.java)
        .localAddress(socketAddress)
    val initializer: ChannelInitializer<SocketChannel> = object : ChannelInitializer<SocketChannel>() {
      override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()
        if (setting.debugMode()) {
          pipeline.addLast(LoggingHandler(LogLevel.DEBUG))
        }
        pipeline.addLast("encoder", TransportMessageEncoder.getInstance())
        pipeline.addLast("decoder", TransportMessageDecoder.newInstance())
        pipeline.addLast(TransportServerChannelHandler(channelManager))
      }
    }

    bootstrap.childHandler(initializer)
    val channel = bootstrap.bind().sync().channel()
    host = (channel.localAddress() as InetSocketAddress).address.hostAddress
    port = (channel.localAddress() as InetSocketAddress).port

    logger.debug("Transport server [${setting.name()}] started at [$host:$port]")
  }

  internal fun close() {
    channelManager.terminate()
    channelManager.awaitTermination(15, TimeUnit.SECONDS)
    channelManager.close()
    boss.shutdownGracefully().awaitUninterruptibly()
    worker.shutdownGracefully().awaitUninterruptibly()
    logger.debug("Transport server [${setting.name()}] closed")
  }
}