package org.goblinframework.transport.server.channel

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.goblinframework.api.function.Disposable
import org.goblinframework.core.util.NetworkUtils
import org.goblinframework.transport.core.codec.TransportMessageDecoder
import org.goblinframework.transport.core.codec.TransportMessageEncoder
import org.goblinframework.transport.server.setting.TransportServerSetting
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

class TransportServerImpl(val setting: TransportServerSetting) : Disposable {

  private val boss: NioEventLoopGroup
  private val worker: NioEventLoopGroup
  internal val channelManager: TransportServerChannelManager
  internal val host: String
  internal val port: Int

  init {
    val socketAddress = InetSocketAddress(setting.host(), setting.port())

    boss = NioEventLoopGroup(setting.threadPoolSetting().bossThreads())
    worker = NioEventLoopGroup(setting.threadPoolSetting().workerThreads())

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
    var h = (channel.localAddress() as InetSocketAddress).address.hostAddress
    if (h == "0:0:0:0:0:0:0:0") {
      h = NetworkUtils.ALL_HOST
    }
    host = h
    port = (channel.localAddress() as InetSocketAddress).port
  }

  override fun dispose() {
    channelManager.terminate()
    channelManager.awaitTermination(15, TimeUnit.SECONDS)
    channelManager.dispose()
    boss.shutdownGracefully().awaitUninterruptibly()
    worker.shutdownGracefully().awaitUninterruptibly()
  }
}