package org.goblinframework.embedded.netty.provider

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.FullHttpRequest
import org.goblinframework.embedded.core.setting.ServerSetting
import java.util.concurrent.ThreadPoolExecutor

class NettyHttpRequestHandler(private val setting: ServerSetting,
                              private val executor: ThreadPoolExecutor) : SimpleChannelInboundHandler<FullHttpRequest>() {

  override fun channelRead0(ctx: ChannelHandlerContext, msg: FullHttpRequest) {
    executor.submit { onRequest(ctx, msg) }
  }

  override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
    ctx?.close()
  }

  private fun onRequest(ctx: ChannelHandlerContext, msg: FullHttpRequest) {

  }
}