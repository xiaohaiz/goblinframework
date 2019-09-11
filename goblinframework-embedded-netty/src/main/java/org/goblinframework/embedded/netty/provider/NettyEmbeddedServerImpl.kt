package org.goblinframework.embedded.netty.provider

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.*
import io.netty.handler.timeout.IdleStateHandler
import org.goblinframework.api.common.InitializeException
import org.goblinframework.embedded.core.setting.ServerSetting
import java.net.InetSocketAddress

class NettyEmbeddedServerImpl(private val setting: ServerSetting) {

  companion object {
    private const val MAX_HTTP_CONTENT_LENGTH = 10 * 1024 * 1024
  }

  val boss: NioEventLoopGroup
  val worker: NioEventLoopGroup
  val host: String
  val port: Int

  init {
    boss = NioEventLoopGroup(1)
    worker = NioEventLoopGroup(4)

    val bootstrap = ServerBootstrap()
        .group(boss, worker)
        .channel(NioServerSocketChannel::class.java)
        .localAddress(setting.networkSetting().toInetSocketAddress())
        .childHandler(object : ChannelInitializer<SocketChannel>() {
          override fun initChannel(ch: SocketChannel) {
            val pipeline = ch.pipeline()
            pipeline.addLast("idleStateHandler", IdleStateHandler(60, 0, 0))
            pipeline.addLast("readerIdleConnectionHandler", ReaderIdleConnectionHandler())
            pipeline.addLast("encoder", HttpResponseEncoder())
            pipeline.addLast("decoder", HttpRequestDecoder())
            pipeline.addLast("httpKeepAlive", HttpServerKeepAliveHandler())
            pipeline.addLast("aggregator", HttpObjectAggregator(MAX_HTTP_CONTENT_LENGTH))
            pipeline.addLast("compressor", HttpContentCompressor())
            pipeline.addLast(NettyHttpRequestHandler(setting))
          }
        })
    val future = bootstrap.bind().sync()
    if (!future.isSuccess) {
      throw InitializeException(future.cause())
    }
    val channel = future.channel()
    host = (channel.localAddress() as InetSocketAddress).address.hostAddress
    port = (channel.localAddress() as InetSocketAddress).port
  }

  internal fun close() {
    boss.shutdownGracefully().awaitUninterruptibly()
    worker.shutdownGracefully().awaitUninterruptibly()
  }
}